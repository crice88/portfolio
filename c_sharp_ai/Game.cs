using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Timers;

namespace Ninja_Gaiden
{
    class Game
    {
        #region Public variables
        public Dictionary<string, string> compPieces = new Dictionary<string, string>();
        public Dictionary<string, string> humanPieces = new Dictionary<string, string>();
        public GameBoard board = new GameBoard();
        public string winner = string.Empty;

        public static MoveGenerator generator;
        public static bool humanTurn = false;
        public static bool endGame = false;
        #endregion

        // Sets up default game board and begins the game loop.
        public Game()
        {
            this.SetupPieces();
            this.CreateBoard();
            this.SetupMoveGenerator();
            this.PrintBoard();
            this.GetFirstPlayer();
            
            while (!endGame)
            {
                this.GetMove();
    
                this.PrintBoard();
            }

            Console.WriteLine(winner);
            Console.Read();
        }

        #region Private Methods
        // Creates static move generator based on initial board pieces.
        private void SetupMoveGenerator()
        {
            Game.generator = new MoveGenerator(compPieces, humanPieces);
        }
        // Creates the board array used for printing the board.
        private void CreateBoard()
        {
            this.board.Create();
        }
        // Prints the current game board.
        private void PrintBoard()
        {
            this.board.Print(this.compPieces, this.humanPieces);
        }
        /// <summary>
        /// Sets all pieces into corresponding dictionaries.
        /// </summary>
        private void SetupPieces()
        {
            this.compPieces.Add("d8", GamePiece.King);
            this.humanPieces.Add("d1", GamePiece.King);

            this.compPieces.Add("a7", GamePiece.Ninja);
            this.compPieces.Add("b7", GamePiece.Ninja);
            this.compPieces.Add("c7", GamePiece.Ninja);
            this.humanPieces.Add("e2", GamePiece.Ninja);
            this.humanPieces.Add("f2", GamePiece.Ninja);
            this.humanPieces.Add("g2", GamePiece.Ninja);

            this.compPieces.Add("e7", GamePiece.Samurai);
            this.compPieces.Add("f7", GamePiece.Samurai);
            this.compPieces.Add("g7", GamePiece.Samurai);
            this.humanPieces.Add("a2", GamePiece.Samurai);
            this.humanPieces.Add("b2", GamePiece.Samurai);
            this.humanPieces.Add("c2", GamePiece.Samurai);

            this.compPieces.Add("e6", GamePiece.MiniNinja);
            this.compPieces.Add("f6", GamePiece.MiniNinja);
            this.compPieces.Add("g6", GamePiece.MiniNinja);
            this.humanPieces.Add("a3", GamePiece.MiniNinja);
            this.humanPieces.Add("b3", GamePiece.MiniNinja);
            this.humanPieces.Add("c3", GamePiece.MiniNinja);

            this.compPieces.Add("a6", GamePiece.MiniSamurai);
            this.compPieces.Add("b6", GamePiece.MiniSamurai);
            this.compPieces.Add("c6", GamePiece.MiniSamurai);
            this.humanPieces.Add("e3", GamePiece.MiniSamurai);
            this.humanPieces.Add("f3", GamePiece.MiniSamurai);
            this.humanPieces.Add("g3", GamePiece.MiniSamurai);
        }
        /// <summary>
        /// Retrieves who will move first.
        /// </summary>
        private void GetFirstPlayer()
        {
            ConsoleKeyInfo cki;
            char firstPlayer;

            Console.WriteLine("Welcome to the CSc 180 Game - Ninja Gaiden");
            Console.WriteLine("Would you like to go... (A) First or (B) Second? ");
            cki = Console.ReadKey();

            Console.WriteLine();

            firstPlayer = cki.KeyChar;
            
            if (char.ToLower(firstPlayer) == 'a')
            {
                Console.WriteLine("You have chosen to go first.");
                Game.humanTurn = true;
            }
            else
            {
                Console.WriteLine("The computer will go first.");
                Game.humanTurn = false;
            }
        }
        /// <summary>
        /// Gets human or computer move.
        /// </summary>
        private void GetMove()
        {
            string move = string.Empty;
            bool invalidMove = true;

            if (Game.humanTurn)
            {
                while (invalidMove)
                {
                    Game.generator.SetValidMoves(this.humanPieces, false);
                    var moves = Game.generator.GetMoveList();
                    // Check to make sure there is a valid move to play.
                    if (moves.Count == 0)
                    {
                        Console.WriteLine("You have run out of moves");
                        endGame = true;
                        winner = "Computer Wins!";
                        break;
                    }

                    Game.generator.PrintValidMoves();
                    Console.WriteLine("Please enter a move: ");
                    move = Console.ReadLine().ToLower();
                    // Make sure move is valid.
                    if (moves.Contains(move))
                    {
                        Game.generator.MakeMove(move);
                        Console.WriteLine("Moving from " + move.Substring(0,2).ToUpper() + " to " + move.Substring(2,2).ToUpper());
                        if (Game.generator.CanAttack(move.Substring(2,2), false))
                        {
                            string piece = Game.generator.Attack(move.Substring(2, 2), false);
                            this.PrintAttack();
                            if (piece == "K")
                            {
                                endGame = true;
                                winner = "You Win!";
                            }
                        }
                        invalidMove = false;
                    } 
                }
            }  
            else
            {
                while (invalidMove)
                {
                    // Generates computer move based on minimax algorithm.
                    move = this.GetComputerMove();
                    if (move == null)
                    {
                        Console.WriteLine("Computer has run out of moves");
                        endGame = true;
                        winner = "You Win!";
                        break;
                    }
                    Game.generator.SetValidMoves(this.compPieces, true);
                    Game.generator.MakeMove(move);
                    Console.WriteLine("Computer moving from " + move.Substring(0, 2).ToUpper() + " to " + move.Substring(2, 2).ToUpper()
                        + " (" + this.MirrorCompMove(move) + ")");
                    
                    if (Game.generator.CanAttack(move.Substring(2, 2), true))
                    {
                        string piece = Game.generator.Attack(move.Substring(2, 2), true);
                        this.PrintAttack();
                        if (piece == "K")
                        {
                            endGame = true;
                            winner = "Computer Wins!";
                        }     
                    }

                    invalidMove = false;
                }
            }
            // Change control to next player.
            Game.generator.NextTurn();
        }
        /// <summary>
        /// Starts stopwatch and uses iterative deepening
        /// to call the minimax algorithm.
        /// </summary>
        /// <returns></returns>
        private string GetComputerMove()
        {
            Computer comp = new Computer();
            string move = string.Empty;
            int i = 1;
            Stopwatch sw = new Stopwatch();
            
            sw.Start();
            // Stop at 500 milliseconds so turn takes 5 seconds or less. This
            // number was devised through a trial and error process.
            for (i = 1; sw.ElapsedMilliseconds < 500; i++ )
            {
                move = comp.MiniMax(i);
                if (move == null)
                    break;
            }
            sw.Stop();
            Console.WriteLine("Searched " + (i - 1) + " Plies in " + sw.ElapsedMilliseconds / 1000 + " seconds.");

            return move;
        }
        
        private void PrintAttack()
        {
            Console.WriteLine("Hi-YA!");
        }
        /// <summary>
        /// Mirrors the computer move so that other human player
        /// can enter it into their machine. Helps expedite the 
        /// CSc 180 tournament.
        /// </summary>
        /// <param name="move"></param>
        /// <returns></returns>
        private string MirrorCompMove(string move)
        {
            string mirroredMove = string.Empty;
            char[] currCoordinates = move.Substring(0, 2).ToCharArray();
            char[] destCoordinates = move.Substring(2, 2).ToCharArray();

            mirroredMove += Convert.ToChar(('h' - currCoordinates[0]) + 96).ToString();
            mirroredMove += Convert.ToChar(('9' - currCoordinates[1]) + 48).ToString();
            mirroredMove += Convert.ToChar(('h' - destCoordinates[0]) + 96).ToString();
            mirroredMove += Convert.ToChar(('9' - destCoordinates[1]) + 48).ToString();

            return mirroredMove.ToUpper();
        }
        #endregion
    }
}
