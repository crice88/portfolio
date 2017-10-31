typedef unsigned long address_t;
   
typedef enum
{
    Running = 1,
    Ready,
    Sleeping,
    Suspended,
    Waiting,
		Finished
} thread_state;

typedef enum
{
    RoundRobin = 1,
    Lottery
} scheduling_type;
  
struct tcb
{
    struct status *stat;
    int tid;
		int weight;
    thread_state state;
    address_t pc;
    address_t sp;
    sigjmp_buf jbuf;
		void *arg;
		void *func;
    struct tcb *next;
    struct tcb *prev;
		STAILQ_ENTRY(tcb) ready_queue;
		STAILQ_ENTRY(tcb) sleeping_queue;
};
   
struct status
{
    int bursts;
    long exec_time;
		long wait_time;
    long sleep_time;
    long avg_exec_time;
    long avg_wait_time;
};

void context_switch(void);
void print_status(struct tcb *t);
void clean_up(int sig);
void sleep_thread(int sec);
void clean_up_thread(void);
void yield_cpu(void);
void lottery(void);
void add_wait_time();
void* get_thread_result(int tid);
int give_tickets(void);
int set_default_status(struct status *stat);
int random_bounded(int bound);
struct tcb* get_thread(int thread_id);

STAILQ_HEAD(r_stailhead, tcb) ready_head = STAILQ_HEAD_INITIALIZER(ready_head);
STAILQ_HEAD(s_stailhead, tcb) sleeping_head = STAILQ_HEAD_INITIALIZER(sleeping_head);
scheduling_type cpu_scheduling;
bool ready;
struct r_stailhead *r_headp;
struct s_stailhead *s_headp;
int thread_id;
int current_time;
int number_of_running_threads;
int total_weight;
int total_loops;
int *tickets;
