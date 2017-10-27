/*
	Colin Rice
	Dr. Dai CSC 139
	User-thread Library
	05/04/2017
*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>
#include <signal.h>
#include <setjmp.h>
#include <stdbool.h>
#include <sys/queue.h>
#include <sys/time.h>

#include "threading.h"

#define SECOND 1000000
#define MAX_THREAD_NUMBER 100
#define STACK_SIZE 4096
#define TIME_QUANTUM 1
#define TIME_INCREMENT 1000
   
///////////////////////////////////////////////////
/**********64-bit architecture**********/
#ifdef __x86_64__
#define JB_SP 6
#define JB_PC 7
   
address_t translate_address(address_t addr)
{
    address_t ret;
    asm volatile("xor    %%fs:0x30,%0\n"
        "rol    $0x11,%0\n"
                 : "=g" (ret)
                 : "0" (addr));
    return ret;
}
   
/**********32-bit architecture**********/
#else
   
#define JB_SP 4
#define JB_PC 5 
   
address_t translate_address(address_t addr)
{
    address_t ret;
    asm volatile("xor    %%gs:0x18,%0\n"
        "rol    $0x9,%0\n"
                 : "=g" (ret)
                 : "0" (addr));
    return ret;
}
#endif
///////////////////////////////////////////////   

/*
	Removes first element from ready queue and
	inserts it at the tail of the list. Next element
	in list will be new first element.
*/
int ready_dequeue()
{
	struct tcb *t;
	struct tcb *t1;
	
	t = STAILQ_FIRST(&ready_head);
	
	if (t == NULL)
	{
		return -1;
	}
	
	STAILQ_REMOVE_HEAD(&ready_head, ready_queue);
	STAILQ_INSERT_TAIL(&ready_head, t, ready_queue);
	
	return 0;
}

/*
	Creates a thread and sets its stack pointer
	to the address of f.
*/  
int create_thread(void(*f)(void))
{
    address_t sp;
    address_t pc;
    char *stack;
    struct tcb *t;
    struct status *s;
       
    if (f == NULL)
    {
        return -1;
    }

    thread_id++;
    number_of_running_threads++;
       
    stack = malloc(STACK_SIZE); 
    if (stack == NULL)
    {
        return -1;
    }
       
    t = malloc(sizeof(struct tcb));
    if (t == NULL)
    {
        return -1;
    }
       
    s = malloc(sizeof(struct status));
    if (s == NULL)
    {
        return -1;
    }
       
    sp = (address_t)stack + STACK_SIZE - sizeof(address_t);
    pc = (address_t)f;
       
    if (set_default_status(s))
    {
        return -1;
    }
       
    sigsetjmp(t->jbuf, 1);
    (t->jbuf->__jmpbuf)[JB_SP] = translate_address(sp);
    (t->jbuf->__jmpbuf)[JB_PC] = translate_address(pc);
    sigemptyset(&(t->jbuf)->__saved_mask);
       
    t->tid = thread_id;
    t->pc = pc;
    t->sp = sp;
    t->stat = s;
    t->func = f;
    t->weight = (rand() % 10) + 1;
    t->arg = NULL;
    t->prev = NULL;
    t->next = NULL;
	
    // Insert at head if list is empty.
    if (STAILQ_EMPTY(&ready_head))
    {
		STAILQ_INSERT_HEAD(&ready_head, t, ready_queue);
    }
    else
    {
		STAILQ_INSERT_TAIL(&ready_head, t, ready_queue);
    }
      
    return thread_id;
}

/*
	Sets the default values for the status struct. Used
	when thread is first created.
*/   
int set_default_status(struct status *stat)
{
    if (stat == NULL)
    {
        return -1;
    }

    stat->bursts = 0;
    stat->exec_time = 0;
    stat->sleep_time = 0;
    stat->avg_exec_time = 0;
    stat->avg_wait_time = 0;
   
    return 0;
}

/*
	Deletes and frees allocated memory of specified
	thread ID.
*/   
int delete_thread(int thread_id)
{
    struct tcb *t;
     
    t = get_thread(thread_id);
    if (t == NULL)
    {
        return -1;
    }
    
    STAILQ_REMOVE(&ready_head, t, tcb, ready_queue);
	
    free(t);
    t = NULL;
       
    return 0;
}

/*
	Function to be assigned to a thread. Does not return. At
	the end of each iteration, sleep thread so dispatch can be
	called by timer signal.
*/    
void f(void)
{
    int i = 0;
	struct tcb *t;
	
    while(true) 
    {
		t = STAILQ_FIRST(&ready_head);
		printf("Thread %d is running\n", get_my_id());
        t->state = Running;
		++i;
		total_loops++;
        
		// When weight is reached, switch to next thread.
        if (i % t->weight == 0) 
        {
            printf("f: switching\n");
            i = 0;
			t->stat->exec_time += TIME_INCREMENT;
			t->state = Waiting;	
			printf("Thread %d is waiting\n", get_my_id());			
            yield_cpu();
        }
        
		t->stat->exec_time += TIME_INCREMENT;
		t->state = Waiting;	
		printf("Thread %d is waiting\n", get_my_id());		
        sleep_thread(SECOND);
    }
}
   
void g(void)
{
    int i = 0;
    struct tcb *t;
	
    while(true)
    {
		t = STAILQ_FIRST(&ready_head);
		printf("Thread %d is running\n", get_my_id());
        t->state = Running;
		++i;
		total_loops++;
           
        if (i % t->weight == 0) 
        {
            printf("g: switching\n");
            i = 0;
	    	t->stat->exec_time += TIME_INCREMENT;
	    	t->state = Waiting;
            printf("Thread %d is waiting\n", get_my_id());
            yield_cpu();           
        }
		
		t->stat->exec_time += TIME_INCREMENT;
		t->state = Waiting;
		printf("Thread %d is waiting\n", get_my_id());
        sleep_thread(SECOND);
    }
}

/*
	Function to be used with create_thread_with_args(). Passed
	in args is checked for a null value then returned.
*/ 
void* h(void *args)
{	   
    if (args == NULL)
    {
        return NULL;
    }
	
    return args;
}

/*
	Switches out process that is next in the ready queue.
*/   
void context_switch(void)
{
    struct tcb *t;
    struct tcb *t1;
    int ret_val;
	
    t = STAILQ_FIRST(&ready_head);
	
    ret_val = sigsetjmp(t->jbuf,1); 
    
    if (ret_val == 1) 
    {
        return;
    }
	
    t1 = STAILQ_NEXT(t, ready_queue);
	
    siglongjmp(t1->jbuf,1);
}

/*
	Returns requested thread by thread id.
*/   
struct tcb* get_thread(int thread_id)
{
    struct tcb *temp = NULL;
       
    STAILQ_FOREACH(temp, &ready_head, ready_queue)
    {
		if (temp->tid == thread_id)
		{
			return temp;
		}
    }
       
    return NULL;
}

/*
	Returns a thread that has an argument. If the threads
	arg attribute is NULL, go to next thread in ready queue.
*/
struct tcb* get_thread_w_args(int thread_id)
{
    struct tcb *temp = NULL;
       
    STAILQ_FOREACH(temp, &ready_head, ready_queue)
    {
		if (temp->tid == thread_id && temp->arg != NULL)
		{
			return temp;
		}
    }
       
    return NULL;
}

/*
	Returns current threads' ID.
*/   
int get_my_id(void)
{	
    struct tcb *t;
	
    t = STAILQ_FIRST(&ready_head);
	
    if (t == NULL)
    {
		return -1;
    }

    return t->tid;
}

/*
	Main entry point for thread scheduling. This function
	never returns.
*/   
void go(void)
{        
    while(true) 
    {
		int ret_result;
		struct tcb *t = NULL;
		struct tcb *t1 = NULL;
		void *ret_val;

		printf("\n****** Values of threads with arguments ******\n\n");
		
		// Get return values of each thread created with an argument.
		STAILQ_FOREACH(t, &ready_head, ready_queue)
		{
			ret_val = get_thread_result(t->tid);

			if (ret_val != NULL)
			{
				ret_result = (int)(*(int *)ret_val);

				printf("Return value of thread: %d\n", ret_result);
			}
		}
		
        t = STAILQ_FIRST(&ready_head);

        if (t == NULL)
        {
            exit(EXIT_FAILURE);
        }
		
		// Generate lottery tickets if scheduling type is lottery.
		if (cpu_scheduling == Lottery)
		{
			STAILQ_FOREACH(t, &ready_head, ready_queue)
			{
				total_weight += t->weight;
			}
			// Since thread doesn't return, just exit unsuccessfully if tickets
			// can't be malloced.
			tickets = malloc(total_weight * sizeof(int));
			if (tickets == NULL)
			{
				exit(EXIT_FAILURE);
			}

			if (give_tickets())
			{
				exit(EXIT_FAILURE);
			}

			t = STAILQ_FIRST(&ready_head);
			if (t == NULL)
			{
				exit(EXIT_FAILURE);
			}
		}
		
		// Jump into thread that is first in the ready queue.
		ready = true;
		printf("Threads are ready for dispatch\n");
		t->state = Running;
		siglongjmp(t->jbuf, 1);
    }
}

/*
Called by timer. Handles dispatch of each thread in queue according to
schedule_type selected.
*/   
void dispatch(int sig)
{
    struct tcb *t;
       
    if (ready)
    {
	// Add wait time to processes currently not running
	add_wait_time();		
	
	if(cpu_scheduling == RoundRobin)
        {
	    t = STAILQ_FIRST(&ready_head);
	    if (t == NULL)
	    {
		exit(EXIT_FAILURE);
	    }
			
	    t->stat->bursts += 1;
			
            // Suspend a random thread every 10 total loops.
	    if (total_loops % 2 == 0)
	    {
		if (suspend_thread(0) == -1)//rand() % number_of_running_threads) == -1)
		{
	    	    exit(EXIT_FAILURE);
		}
		number_of_running_threads--;
	    }
			
	    // Resume a random thread every 12 total loops.
	    if (total_loops % 3 == 0)
	    {
			if (resume_thread(0) == -1)//rand() % number_of_running_threads) == -1)
			{
					exit(EXIT_FAILURE);
			}
			number_of_running_threads++;
	    }
		if (ready_dequeue())
		{
			exit(EXIT_FAILURE);
		}
    }
	else
	{
		t = STAILQ_FIRST(&ready_head);
		t->stat->bursts += 1;

		if (total_loops % 5 == 0)
		{
			if (suspend_thread(rand() % number_of_running_threads) == -1)
			{
				exit(EXIT_FAILURE);
			}
			number_of_running_threads--;
		}
			
		// Resume a random thread every 12 total loops.
		if (total_loops % 7 == 0)
		{
			if (resume_thread(0) == -1)
			{
				exit(EXIT_FAILURE);
			}
			number_of_running_threads++;
		}		
		lottery();
		}
    }
}

void lottery(void)
{
    int ticket = 0;
    int upper_bound = 0;
    int winner = 0;
    struct tcb *t;
    struct tcb *temp;
    
    // Get the winning ticket. Number of ticket will indicate
    // thread id of process that won.
    upper_bound = (total_weight - 1);
    ticket = random_bounded(upper_bound);

    winner = tickets[ticket];
	
    // Insert winning thread at the head of the list.
    t = get_thread(winner);
    if (t == NULL)
    {
		printf("Thread %d is not in the ready queue and cannot be scheduled.\n", winner);
    }
    else
    {
    	STAILQ_REMOVE(&ready_head, t, tcb, ready_queue);
		STAILQ_INSERT_HEAD(&ready_head, t, ready_queue);
    }
}

void add_wait_time()
{
	struct tcb *t = NULL;
	
	STAILQ_FOREACH(t, &ready_head, ready_queue)
	{
    	if (t->state == Waiting)
	    {
			t->stat->wait_time += TIME_INCREMENT;
	    }
	}
}

/*
	Returns a random number between 0 and bound.
*/
int random_bounded(int bound)
{
    int random_number;
      
    random_number = rand() % (bound);
      
    return random_number;
}

/*
	Dequeues next thread and switches the thread.
*/  
void yield_cpu(void)
{
	ready_dequeue();
	context_switch();
}

/*
	Removes thread from ready queue and inserts it into
	the sleeping queue.
*/   
int suspend_thread(int thread_id)
{
    struct tcb *t;
    struct tcb *temp;
    
    t = get_thread(thread_id);
    temp = STAILQ_FIRST(&ready_head);
    if (t == NULL)
    {
    	printf("Thread %d could not be suspended.\n", thread_id);
		return 0;
    }
    if (temp == NULL)
    {
    	return -1;
    }
    if (temp->tid == thread_id)
    {
    	STAILQ_INSERT_HEAD(&sleeping_head, t, sleeping_queue);
		STAILQ_REMOVE(&ready_head, t, tcb, ready_queue);
		printf("Thread %d was suspended.\n", thread_id);
		yield_cpu();
    }
    else
    {
		STAILQ_INSERT_HEAD(&sleeping_head, t, sleeping_queue);
		STAILQ_REMOVE(&ready_head, t, tcb, ready_queue);
		printf("Thread %d was suspended.\n", thread_id);
    }

    return thread_id;
}

/*
	Removes suspended thread and inserts it back into the 
	ready queue.
*/   
int resume_thread(int thread_id)
{
    struct tcb *t = NULL;
    struct tcb *temp = NULL;
    bool found = false;
	
    STAILQ_FOREACH(t, &sleeping_head, sleeping_queue)
    {
    	if (t->tid == thread_id)
	{
    	    STAILQ_INSERT_TAIL(&ready_head, t, ready_queue);
	    STAILQ_REMOVE(&sleeping_head, t, tcb, sleeping_queue);
	    found = true;
	    temp = t;
	}
    }
	
    if (found)
    {
	printf("Thread %d was resumed\n", thread_id);
	temp->state = Ready;
    }
    else
    {
    	printf("Thread %d was not in sleeping queue.\n", thread_id);
    }
	
    return thread_id;
}

/*
	Assigns final stats to status struct.
*/   
int get_status(int thread_id, struct status *stat)
{
    struct tcb *t;

    t = get_thread(thread_id);
    if (t == NULL)
    {
        return -1;
    }
     
    if (stat == NULL)
    {
        return -1;
    }
     
    stat->bursts = t->stat->bursts;
    stat->exec_time = t->stat->exec_time;
    stat->sleep_time = t->stat->sleep_time;
    stat->avg_exec_time = (t->stat->exec_time / number_of_running_threads);
    stat->avg_wait_time = (t->stat->wait_time / number_of_running_threads);
	
    t->stat = stat;
     
    return t->tid;
}

/*
	Assigns final stats to sleeping threads.
*/
int get_sleep_thread_status(struct tcb *t, struct status *stat)
{
    if (t == NULL)
    {
        return -1;
    }
	
    if (stat == NULL)
    {
        return -1;
    }
	
    stat->bursts = t->stat->bursts;
    stat->exec_time = t->stat->exec_time;
    stat->sleep_time = t->stat->sleep_time;
    stat->avg_exec_time = (t->stat->exec_time / number_of_running_threads);
    stat->avg_wait_time = (t->stat->wait_time / number_of_running_threads);
	
    t->stat = stat;
     
    return t->tid;
}

/*
	Sleeps the current thread by sec. Increase
	sleep time.
*/   
void sleep_thread(int sec)
{
	struct tcb *t;
	
	t = STAILQ_FIRST(&ready_head);
	t->stat->sleep_time += TIME_INCREMENT;
	printf("Thread %d is sleeping\n", get_my_id());
	usleep(sec);
	printf("Thread %d is ready\n", get_my_id());
}

/*
	Cleans up all threads. Ends scheduling and, on success,
	returns EXIT_SUCCESS.
*/  
void clean_up(int sig)
{
	struct status s;
	struct tcb *ready = NULL;
	struct tcb *sleep = NULL;
	
	// Free threads in ready queue.
	if (!STAILQ_EMPTY(&ready_head))
	{
		STAILQ_FOREACH(ready, &ready_head, ready_queue)
		{
			if (get_status(ready->tid, &s) == -1)
			{
				exit(EXIT_FAILURE);
			}
			else
			{
				print_status(ready);
				free(ready);
			}
		}
		ready = NULL;
	}
	
	// Free threads in sleeping queue.
	if (!STAILQ_EMPTY(&sleeping_head))
	{
	    STAILQ_FOREACH(sleep, &sleeping_head, sleeping_queue)
		{
			if (get_sleep_thread_status(sleep, &s) == -1)
			{
				exit(EXIT_FAILURE);
			}
			else
			{
				print_status(sleep);
				free(sleep);
			}
		}
		sleep = NULL;
	}
	
    exit(EXIT_SUCCESS);
}

/*
	Creates a thread with arguments. Function pointer is then assigned to
	func attribute in TCB struct.
*/ 
int create_thread_with_args(void *(*f)(void *), void *arg)
{
    address_t sp;
    address_t pc;
    address_t rtn_addr;
    char *stack;
    struct tcb *t;
    struct status *s;
    
    if (arg == NULL)
    {
        return -1;
    } 

    if (f == NULL)
    {
        return -1;
    }

    thread_id++;
       
    stack = malloc(STACK_SIZE); 
    if (stack == NULL)
    {
        return -1;
    }
       
    t = malloc(sizeof(struct tcb));
    if (t == NULL)
    {
        return -1;
    }
       
    s = malloc(sizeof(struct status));
    if (s == NULL)
    {
        return -1;
    }
    
    sp = (address_t)stack + STACK_SIZE - sizeof(address_t);
    pc = (address_t)f;
     
    if (set_default_status(s))
    {
        return -1;
    }
       
    sigsetjmp(t->jbuf, 1);
    (t->jbuf->__jmpbuf)[JB_SP] = translate_address(sp);
    (t->jbuf->__jmpbuf)[JB_PC] = translate_address(pc);
    sigemptyset(&(t->jbuf)->__saved_mask);
       
    t->tid = thread_id;
    t->pc = pc;
    t->sp = sp;
	t->weight = (rand() % 10) + 1;	
    t->stat = s;
    t->prev = NULL;
    t->next = NULL;
	t->arg = arg;
	t->func = (*f)(t->arg);
    
	if (STAILQ_EMPTY(&ready_head))
	{
		STAILQ_INSERT_HEAD(&ready_head, t, ready_queue);
	}
	else
	{
		STAILQ_INSERT_TAIL(&ready_head, t, ready_queue);
	}
     
    return t->tid;
}

/*
	Returns parameters of thread that was created with 
	arguments.
*/ 
void* get_thread_result(int tid)
{
    struct tcb *t;
	void *i;
	
	t = get_thread_w_args(tid);
    if (t == NULL)
    {
        return NULL;
    }

	i = t->func;
    if (i == NULL)
    {
        return NULL;
    }

	t->state = Finished;
	delete_thread(t->tid);
	
	return i;
}

/*
	Prints the status of tcb struct.
*/ 
void print_status(struct tcb *t)
{
    if (t == NULL)
    {
        exit(EXIT_FAILURE);
    }
     
	printf("****** Thread %d Stats ******\n", t->tid);
	printf("Number of Bursts: %d\n", t->stat->bursts);
	printf("Execution Time: %ld msecs\n", t->stat->exec_time);
	printf("Sleeping Time: %ld msecs\n",t->stat->sleep_time);
	printf("Average Execution Time: %ld msecs\n", t->stat->avg_exec_time);
	printf("Average Waiting Time: %ld msecs\n\n", t->stat->avg_wait_time);
    
}

/*
	Fills an array with tickets based on threads weight and
	the total weight of all processes. Tickets are assigned 
	by thread id so if that id is chosen from the array, 
	it is the winning thread.
*/ 
int give_tickets(void)
{
	struct tcb *t = NULL;
    int j = 0;
	int total_tickets = 0;

    if (tickets == NULL)
    {
        return -1;
    }
	
    STAILQ_FOREACH(t, &ready_head, ready_queue)
	{
		while (j <= (t->weight + total_tickets))
		{
			tickets[j] = t->tid;
			j++;
		}
		total_tickets = j;
	}
	
	return 0;
}

/*
	Asks the user what scheduling type they would like to use.
*/
void get_user_default()
{
	int schedule_type;
	bool invalid_answer = true;
	
	while (invalid_answer)
	{
		printf("What scheduling type would you like to use? (1 - Round-robin, 2 - Lottery)\n");
		if (scanf("%d", &schedule_type) != 1)
		{
			printf("Please choose either a '1' or '2' indicating schedule type selection.\n");
			exit(EXIT_FAILURE);
		}
		else if (schedule_type == RoundRobin || schedule_type == Lottery)
		{
			invalid_answer = false;
		}
	}
	
    cpu_scheduling = schedule_type;
} 

int main(int argc, char *argv[])
{   
    int tid;
	int i;
	int *rand_num;
    struct tcb *t;
    struct itimerval tv;
       
    thread_id = -1;
    ready = false;
    srand(time(NULL));
    
	// Setup signal handlers
    if (signal(SIGALRM, dispatch) == SIG_ERR)
    {
        return -1;
    }

    if (signal(SIGINT, clean_up) == SIG_ERR)
    {
        return -1;
    }

	// Create tail Q heads
	STAILQ_INIT(&ready_head);
	STAILQ_INIT(&sleeping_head);
    get_user_default();
      
	// Set timer intervals
    tv.it_value.tv_sec = TIME_QUANTUM;
    tv.it_value.tv_usec = 0;
    tv.it_interval.tv_sec = TIME_QUANTUM;
    tv.it_interval.tv_usec = 0;
    
	// Create n number of threads.
    for (i = 0; i < 10; i++)
	{
		create_thread(f);
		create_thread(g);
		rand_num = malloc(sizeof(int *));
		
		if (rand_num == NULL)
		{
			return -1;
		}
		
		*rand_num = rand() % 1000;
		create_thread_with_args(h, (void*)rand_num);
	}
    
	// Activate timer.
    if (setitimer(ITIMER_REAL, &tv, NULL))
    {
        return -1;
    }
	
    go();
       
    return 0;
}
