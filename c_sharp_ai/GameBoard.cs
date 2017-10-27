using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Ninja_Gaiden
{
    class GameBoard
    {
        private string[,] board = new string[8, 7];
        // Initializes game board array.
        public void Create()
        {
            char k;

            for (int i = 0; i < 8; i++)
            {
                k = 'a';
                
                for (int j = 0; j < 7; j++)
                {
                    board[i, j] = k + (i + 1).ToString();
                    k++;
                }
            }
        }
        /// <summary>
        /// Prints current board state to std out.
        /// </summary>
        /// <param name="compPieces"></param>
        /// <param name="humanPieces"></param>
        public void Print(Dictionary<string, string> compPieces, Dictionary<string, string> humanPieces)
        {
            int k = 0;
            Console.WriteLine("  -------  Computer");

            for (int i = 7; i >= 0; i--)
            {
                Console.Write((i + 1) + " ");
                for (int j = 0; j < 7; j++, k++)
                {
                    if (k % 2 == 0)
                    {
                        Console.BackgroundColor = ConsoleColor.Blue;
                        Console.ForegroundColor = ConsoleColor.Blue;
                    }     
                    else
                    {
                        this.ResetColor();
                    }

                    if (compPieces.ContainsKey(this.board[i, j]))
                    {
                        Console.ForegroundColor = ConsoleColor.Red;
                        Console.Write(compPieces[this.board[i,j]]);
                    }
                    else if (humanPieces.ContainsKey(this.board[i,j]))
                    {
                        Console.ForegroundColor = ConsoleColor.White;
                        Console.Write(humanPieces[this.board[i,j]]);
                    }
                    else
                    {
                        Console.Write("A");
                    }
                }
                Console.ResetColor();
                Console.WriteLine();
            }

            Console.WriteLine();
            Console.Write("  ");

            for (int i = 0, j = 0; i < 7; i++ )
            {
                Console.Write(this.board[j, i].Remove(1).ToUpper());
            }

            Console.WriteLine("\n  -------  Human");
            Console.WriteLine();
        }
        // Resets the color displayed on the terminal.
        private void ResetColor()
        {
            Console.ResetColor();
            Console.BackgroundColor = ConsoleColor.Black;
            Console.ForegroundColor = ConsoleColor.Black;
        }
    }
}
