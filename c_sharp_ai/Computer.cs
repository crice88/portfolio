using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Ninja_Gaiden
{
    /// <summary>
    /// Encapsulates computers best move generation
    /// using Minimax algorithm and alpha-beta pruning.
    /// </summary>
    class Computer
    {
        private int maxDepth = 0;

        /// <summary>
        /// Minimax algorithm entry point.
        /// </summary>
        /// <param name="maxDepth"></param>
        /// <returns></returns>
        public string MiniMax(int maxDepth)
        {
            string bestMove = string.Empty;
            int bestScore = -9999;
            int depth = 0;
            int score;

            this.maxDepth = maxDepth;
            Game.generator.SetValidMoves(Game.generator.GetCompPieces(), true);

            var moveList = Game.generator.GetMoveList();
            if (moveList.Count == 0) return null;

            foreach(string move in moveList)
            {
                string pieceAttacked = string.Empty;
                string current = move.Substring(0, 2);
                string dest = move.Substring(2, 2);

                Game.generator.MakeMove(move);

                if (Game.generator.CanAttack(dest, true))
                {
                    pieceAttacked = Game.generator.Attack(dest, true);
                }

                score = this.Min(depth + 1, 0);
                if (score > bestScore)
                {
                    bestMove = move;
                    bestScore = score;
                }
  
                Game.generator.RetractMove(move, true, pieceAttacked);
            }

            return bestMove;
        }

        #region Private Methods
        private int Max(int depth, int parentScore)
        {
            Dictionary<string, string> humanPieces = Game.generator.GetHumanPieces();
            Dictionary<string, string> computerPieces = Game.generator.GetCompPieces();
            string bestMove = string.Empty;
            int score;
            int bestScore = -9999;
            
            if (this.IsGameOver(computerPieces, true))
            {
                return 999;
            }
            if (depth == this.maxDepth)
            {
                return this.Eval(computerPieces, humanPieces);
            }

            Game.generator.SetValidMoves(computerPieces, true);

            var compMoveList = Game.generator.GetMoveList();

            foreach (string move in compMoveList)
            {
                string pieceAttacked = string.Empty;
                string current = move.Substring(0, 2);
                string dest = move.Substring(2, 2);

                Game.generator.MakeMove(move);

                if (Game.generator.CanAttack(dest, true))
                {
                    pieceAttacked = Game.generator.Attack(dest, true);
                }

                score = this.Min(depth + 1, bestScore);

                if (score > bestScore)
                    bestScore = (score + depth);

                Game.generator.RetractMove(move, true, pieceAttacked);

                if (score > parentScore)
                    return parentScore;
            }
            return bestScore;
        }

        private int Min(int depth, int parentScore)
        {
            Dictionary<string, string> computerPieces = Game.generator.GetCompPieces();
            Dictionary<string, string> humanPieces = Game.generator.GetHumanPieces();
            string bestMove = string.Empty;
            int bestScore = 9999;
            int score;

            if (this.IsGameOver(humanPieces, false))
            {
                return -999;
            }
            if (depth == this.maxDepth)
            {
                return this.Eval(computerPieces, humanPieces);
            }

            Game.generator.SetValidMoves(humanPieces, false);

            var humanMoveList = Game.generator.GetMoveList();

            foreach (string move in humanMoveList)
            {
                string pieceAttacked = string.Empty;
                string current = move.Substring(0, 2);
                string dest = move.Substring(2, 2);

                Game.generator.MakeMove(move);
                    
                if (Game.generator.CanAttack(dest, false))
                {
                    pieceAttacked = Game.generator.Attack(dest, false);
                }

                score = this.Max(depth + 1, bestScore);

                if (score < bestScore)
                    bestScore = (score - depth);

                Game.generator.RetractMove(move, false, pieceAttacked);

                if (score < parentScore)
                    return parentScore;
            }

            return bestScore;
        }

        /// <summary>
        /// Check to see if a game piece is threatening a king 
        /// piece (sitting on tile in front of king).
        /// </summary>
        /// <param name="pieces"></param>
        /// <param name="isComputer"></param>
        /// <returns></returns>
        private bool IsGameOver(Dictionary<string, string> pieces, bool isComputer)
        {
            string humanThreatened = "d2";
            string computerThreatened = "d7";

            if (isComputer)
            {
                return pieces.ContainsKey(humanThreatened);
            }
            else
            {
                return pieces.ContainsKey(computerThreatened);
            }
        }

        /// <summary>
        /// Basic terminal evaluation function that totals
        /// all pieces on the board for both computer and human
        /// and finds the difference between them.
        /// </summary>
        /// <param name="compPieces"></param>
        /// <param name="humanPieces"></param>
        /// <returns></returns>
        private int Eval(Dictionary<string, string> compPieces, Dictionary<string, string> humanPieces)
        {
            int totalComputerScore = 0;
            int totalHumanScore = 0;

            foreach (KeyValuePair<string, string> piece in compPieces)
            {
                if (piece.Value == GamePiece.Ninja)
                    totalComputerScore += 20;
                else if (piece.Value == GamePiece.Samurai)
                    totalComputerScore += 20;
                else if (piece.Value == GamePiece.MiniNinja)
                    totalComputerScore += 10;
                else if (piece.Value == GamePiece.MiniSamurai)
                    totalComputerScore += 10;
            }

            foreach (KeyValuePair<string, string> piece in humanPieces)
            {
                if (piece.Value == GamePiece.Ninja)
                    totalHumanScore += 20;
                else if (piece.Value == GamePiece.Samurai)
                    totalHumanScore += 20;
                else if (piece.Value == GamePiece.MiniNinja)
                    totalHumanScore += 10;
                else if (piece.Value == GamePiece.MiniSamurai)
                    totalHumanScore += 10;
            }

            return totalComputerScore - totalHumanScore;
        }
        #endregion
    }
}
