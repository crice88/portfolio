using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Ninja_Gaiden
{
    class MoveGenerator
    {
        #region Private Variables
        private Dictionary<string, string> compPieces = new Dictionary<string, string>();
        private Dictionary<string, string> humanPieces = new Dictionary<string, string>();

        private List<string> validMoves = new List<string>();
        // Set lower and upper bound of the game board.
        private readonly char xLowerBound = 'a';
        private readonly char xUpperBound = 'g';
        private readonly char yLowerBound = '1';
        private readonly char yUpperBound = '8';
        #endregion

        #region Public Methods
        public MoveGenerator(Dictionary<string, string> compPieces, Dictionary<string, string> humanPieces)
        {
            this.compPieces = compPieces;
            this.humanPieces = humanPieces;
        }
        /// <summary>
        /// Sets all valid moves for the piece set.
        /// </summary>
        /// <param name="pieces"></param>
        /// <param name="isComputer"></param>
        public void SetValidMoves(Dictionary<string, string> pieces, bool isComputer)
        {
            this.validMoves.Clear();

            foreach (KeyValuePair<string, string> entry in pieces)
            {
                if (!isComputer)
                {
                    if (entry.Value == GamePiece.Ninja)
                    {
                        this.GetValidNinjaMoves(entry.Key, false);
                    }
                    else if (entry.Value == GamePiece.Samurai)
                    {
                        this.GetValidSamuraiMoves(entry.Key, false);
                    }
                    else if (entry.Value == GamePiece.MiniNinja)
                    {
                        this.GetValidMiniNinjaMoves(entry.Key, false);
                    }
                    else if (entry.Value == GamePiece.MiniSamurai)
                    {
                        this.GetValidMiniSamuraiMoves(entry.Key, false);
                    }
                }
                else if (isComputer)
                {
                    if (entry.Value == GamePiece.Ninja)
                    {
                        this.GetValidNinjaMoves(entry.Key, true);
                    }
                    else if (entry.Value == GamePiece.Samurai)
                    {
                        this.GetValidSamuraiMoves(entry.Key, true);
                    }
                    else if (entry.Value == GamePiece.MiniNinja)
                    {
                        this.GetValidMiniNinjaMoves(entry.Key, true);
                    }
                    else if (entry.Value == GamePiece.MiniSamurai)
                    {
                        this.GetValidMiniSamuraiMoves(entry.Key, true);
                    }
                }
            }
        }

        public void PrintValidMoves()
        {
            Console.WriteLine("Valid Moves are: ");

            foreach (string move in validMoves)
            {
                Console.Write(move.ToUpper() + "    ");
            }

            Console.WriteLine();
        }
        /// <summary>
        /// Makes certain that the piece exists, that the 
        /// spot it is moving too is unoccupied and that the
        /// move is within the board bounds.
        /// </summary>
        /// <param name="currentPos"></param>
        /// <param name="destinationPos"></param>
        /// <param name="isComputer"></param>
        /// <returns></returns>
        public bool ValidateMove(string currentPos, string destinationPos, bool isComputer)
        {
            // Make sure that the piece being moved actually exists.
            if (isComputer)
            {
                return  this.compPieces.ContainsKey(currentPos) &&
                        !this.IsSpotOccupied(destinationPos)    && 
                        this.IsWithinBoardBounds(destinationPos);
            }
            else if (!isComputer)
            {
                return this.humanPieces.ContainsKey(currentPos) &&
                       !this.IsSpotOccupied(destinationPos)     &&
                       this.IsWithinBoardBounds(destinationPos);
            }

            return false;
        }

        // Check to see if the destination spot is occupied.
        private bool IsSpotOccupied(string destinationPos)
        {
            return this.compPieces.ContainsKey(destinationPos) || this.humanPieces.ContainsKey(destinationPos);
        }
        
        public bool CanAttack(string offensePos, bool isComputer)
        {
            char[] offenseCoordinates = offensePos.ToCharArray();

            char x = char.ToLower(offenseCoordinates[0]);
            char y = char.ToLower(offenseCoordinates[1]);

            if (isComputer)
            {
                foreach (KeyValuePair<string, string> defense in this.humanPieces)
                {
                    char[] defenseCoordinates = defense.Key.ToCharArray();
                    char defenseX = char.ToLower(defenseCoordinates[0]);
                    char defenseY = char.ToLower(defenseCoordinates[1]);

                    if ((x - defenseX) == 0 &&
                        (y - defenseY) == 1)
                    {
                        return true;
                    }
                }
            }
            else
            {
                foreach (KeyValuePair<string, string> defense in this.compPieces)
                {
                    char[] defenseCoordinates = defense.Key.ToCharArray();
                    char defenseX = char.ToLower(defenseCoordinates[0]);
                    char defenseY = char.ToLower(defenseCoordinates[1]);

                    if ((x - defenseX) == 0 &&
                        (y - defenseY) == -1)
                    {
                        return true;
                    }
                }
                
            }

            return false;
        }

        public string Attack(string offense, bool isComputer)
        {
            string opponentKey = string.Empty;

            char[] offenseCoordinates = offense.ToCharArray();

            char x = offenseCoordinates[0];
            char y = offenseCoordinates[1];

            if (isComputer)
            {
                y = this.IncrementChar(y, -1);
                opponentKey = x.ToString() + y.ToString();

                return this.DecrementPiece(opponentKey, isComputer);
            }
            else
            {
                y = this.IncrementChar(y, 1);
                opponentKey = x.ToString() + y.ToString();

                return this.DecrementPiece(opponentKey, isComputer);
            }
        }

        public string DecrementPiece(string coordinates, bool isComputer)
        {
            char[] defenseCoordinates = coordinates.ToCharArray();
            char x = char.ToLower(defenseCoordinates[0]);
            char y = char.ToLower(defenseCoordinates[1]);
            Dictionary<string, string> pieces;

            if (isComputer)
                pieces = this.humanPieces;
            else
                pieces = this.compPieces;

            string pieceType = pieces[coordinates];

            if (pieceType == GamePiece.King)
            {
                return GamePiece.King;
            }
            else if (pieceType == GamePiece.Samurai)
            {
                pieces.Remove(coordinates);
                pieces.Add(coordinates, GamePiece.MiniSamurai);
                return GamePiece.Samurai;
            }
            else if (pieceType == GamePiece.Ninja)
            {
                pieces.Remove(coordinates);
                pieces.Add(coordinates, GamePiece.MiniNinja);
                return GamePiece.Ninja;
            }
            else if (pieceType == GamePiece.MiniNinja)
            {
                pieces.Remove(coordinates);
                return GamePiece.MiniNinja;
            }
            else if (pieceType == GamePiece.MiniSamurai)
            {
                pieces.Remove(coordinates);
                return GamePiece.MiniSamurai;
            }
            else
                return string.Empty;
        }

        public void MakeMove(string move)
        {
            string currentPos = string.Empty;

            currentPos = move.Substring(0, 2).ToLower();
            this.MovePiece(move, false, this.compPieces.ContainsKey(currentPos));
        }

        public void NextTurn()
        {
            if (Game.humanTurn)
                Game.humanTurn = false;
            else
                Game.humanTurn = true;
        }
        /// <summary>
        /// Retracts the move that was played. Used in the 
        /// minimax algorithm.
        /// </summary>
        /// <param name="move"></param>
        /// <param name="isComputer"></param>
        /// <param name="pieceAttacked"></param>
        public void RetractMove(string move, bool isComputer, string pieceAttacked)
        {
            string destination = move.Substring(2, 2);
            char[] coordinates = destination.ToCharArray();
            char x = coordinates[0];
            char y = coordinates[1];

            this.MovePiece(move, true, isComputer);

            if (pieceAttacked != string.Empty)
            {
                if (isComputer)
                {
                    string human = this.IncrementChar(x, 0).ToString() + this.IncrementChar(y, -1).ToString();

                    if (pieceAttacked == GamePiece.Ninja)
                        this.humanPieces[human] = pieceAttacked;
                    else if (pieceAttacked == GamePiece.Samurai)
                        this.humanPieces[human] = pieceAttacked;
                    else if (pieceAttacked == GamePiece.King)
                        return;
                    else
                        this.humanPieces.Add(human, pieceAttacked);
                }
                else
                {
                    string computer = this.IncrementChar(x, 0).ToString() + this.IncrementChar(y, 1).ToString();

                    if (pieceAttacked == GamePiece.Ninja)
                        this.compPieces[computer] = pieceAttacked;
                    else if (pieceAttacked == GamePiece.Samurai)
                        this.compPieces[computer] = pieceAttacked;
                    else if (pieceAttacked == GamePiece.King)
                        return;
                    else
                        this.compPieces.Add(computer, pieceAttacked);
                }
            }
        }

        public List<string> GetMoveList()
        {
            return new List<string>(this.validMoves);
        }

        public Dictionary<string, string> GetCompPieces()
        {
            return this.compPieces;
        }

        public Dictionary<string, string> GetHumanPieces()
        {
            return this.humanPieces;
        }
        #endregion

        #region Private Methods
        // Make sure move is within the boards bounds.
        private bool IsWithinBoardBounds(string destinationPos)
        {
            char[] coordinates = destinationPos.ToCharArray();

            if (coordinates.Length > 2)
                return false;

            char x = char.ToLower(coordinates[0]);
            char y = char.ToLower(coordinates[1]);

            if (x > xUpperBound || x < xLowerBound ||
                y > yUpperBound || y < yLowerBound)
            {
                return false;
            }

            return true;
        }

        private void GetValidNinjaMoves(string currentPosition, bool isComputer)
        {
            List<string> moves = new List<string>();

            moves = this.NinjaMoves(currentPosition, isComputer);

            if (moves.Count > 0)
            {
                this.validMoves.AddRange(moves);
            }
        }

        private void GetValidSamuraiMoves(string currentPosition, bool isComputer)
        {
            List<string> moves = new List<string>();

            moves = this.SamuraiMoves(currentPosition, isComputer);

            if (moves.Count > 0)
            {
                this.validMoves.AddRange(moves);
            }
        }

        private void GetValidMiniNinjaMoves(string currentPosition, bool isComputer)
        {
            List<string> moves = new List<string>();

            moves = this.MiniNinjaMoves(currentPosition, isComputer);

            if (moves.Count > 0)
            {
                this.validMoves.AddRange(moves);
            }
        }

        private void GetValidMiniSamuraiMoves(string currentPosition, bool isComputer)
        {
            List<string> moves = new List<string>();

            moves = this.MiniSamuraiMoves(currentPosition, isComputer);

            if (moves.Count > 0)
            {
                this.validMoves.AddRange(moves);
            }
        }

        private List<string> MiniNinjaMoves(string currentPosition, bool isComputer)
        {
            List<string> moves = new List<string>();
            string destination = string.Empty;
            char[] coordinates = currentPosition.ToCharArray();
            char xPos = ' ';
            char yPos = ' ';
            
            if (coordinates.Length == 2)
            {
                xPos = coordinates[0];
                yPos = coordinates[1];
            }
            else
            {
                return null;
            }

            if (isComputer)
            {
                destination = this.IncrementChar(xPos, 1).ToString() +
                              this.IncrementChar(yPos, -1).ToString();

                if (this.ValidateMove(currentPosition, destination, isComputer))
                {
                    moves.Add(currentPosition + destination);
                }

                destination = this.IncrementChar(xPos, -1).ToString() +
                              this.IncrementChar(yPos, -1).ToString();

                if (this.ValidateMove(currentPosition, destination, isComputer))
                {
                    moves.Add(currentPosition + destination);
                }

                destination = this.IncrementChar(xPos, 1).ToString() +
                              this.IncrementChar(yPos, 1).ToString();

                if (this.ValidateMove(currentPosition, destination, isComputer) && this.CanAttack(destination, isComputer))
                {
                    moves.Add(currentPosition + destination);
                }

                destination = this.IncrementChar(xPos, -1).ToString() +
                              this.IncrementChar(yPos, 1).ToString();

                if (this.ValidateMove(currentPosition, destination, isComputer) && this.CanAttack(destination, isComputer))
                {
                    moves.Add(currentPosition + destination);
                }
            }
            else
            {
                destination = this.IncrementChar(xPos, 1).ToString() +
                              this.IncrementChar(yPos, 1).ToString();

                if (this.ValidateMove(currentPosition, destination, isComputer))
                {
                    moves.Add(currentPosition + destination);
                }

                destination = this.IncrementChar(xPos, -1).ToString() +
                              this.IncrementChar(yPos,  1).ToString();

                if (this.ValidateMove(currentPosition, destination, isComputer))
                {
                    moves.Add(currentPosition + destination);
                }

                destination = this.IncrementChar(xPos, -1).ToString() +
                              this.IncrementChar(yPos, -1).ToString();

                if (this.ValidateMove(currentPosition, destination, isComputer) && this.CanAttack(destination, isComputer))
                {
                    moves.Add(currentPosition + destination);
                }

                destination = this.IncrementChar(xPos, 1).ToString() +
                              this.IncrementChar(yPos, -1).ToString();

                if (this.ValidateMove(currentPosition, destination, isComputer) && this.CanAttack(destination, isComputer))
                {
                    moves.Add(currentPosition + destination);
                }
            }

            return moves;
        }

        private List<string> MiniSamuraiMoves(string currentPosition, bool isComputer)
        {
            List<string> moves = new List<string>();
            string destination = string.Empty;
            char[] coordinates = currentPosition.ToCharArray();
            char xPos = ' ';
            char yPos = ' ';

            if (coordinates.Length == 2)
            {
                xPos = coordinates[0];
                yPos = coordinates[1];
            }
            else
            {
                return null;
            }

            if (isComputer)
            {
                destination = this.IncrementChar(xPos, 0).ToString() +
                              this.IncrementChar(yPos, -1).ToString();

                if (this.ValidateMove(currentPosition, destination, isComputer))
                {
                    moves.Add(currentPosition + destination);
                }

                destination = this.IncrementChar(xPos, 1).ToString() +
                              this.IncrementChar(yPos, 0).ToString();

                if (this.ValidateMove(currentPosition, destination, isComputer) && this.CanAttack(destination, isComputer))
                {
                    moves.Add(currentPosition + destination);
                }

                destination = this.IncrementChar(xPos, -1).ToString() +
                              this.IncrementChar(yPos, 0).ToString();

                if (this.ValidateMove(currentPosition, destination, isComputer) && this.CanAttack(destination, isComputer))
                {
                    moves.Add(currentPosition + destination);
                }
            }
            else
            {
                destination = this.IncrementChar(xPos, 0).ToString() +
                              this.IncrementChar(yPos, 1).ToString();

                if (this.ValidateMove(currentPosition, destination, isComputer))
                {
                    moves.Add(currentPosition + destination);
                }

                destination = this.IncrementChar(xPos, 1).ToString() +
                              this.IncrementChar(yPos, 0).ToString();

                if (this.ValidateMove(currentPosition, destination, isComputer) && this.CanAttack(destination, isComputer))
                {
                    moves.Add(currentPosition + destination);
                }

                destination = this.IncrementChar(xPos, -1).ToString() +
                              this.IncrementChar(yPos, 0).ToString();

                if (this.ValidateMove(currentPosition, destination, isComputer) && this.CanAttack(destination, isComputer))
                {
                    moves.Add(currentPosition + destination);
                }
            }

            return moves;
        }

        private List<string> SamuraiMoves(string currentPosition, bool isComputer)
        {
            List<string> moves = new List<string>();
            string destination = string.Empty;
            char[] coordinates = currentPosition.ToCharArray();
            char xPos = ' ';
            char yPos = ' ';
            char xPosTemp = ' ';
            char yPosTemp = ' ';

            if (coordinates.Length == 2)
            {
                xPos = coordinates[0];
                yPos = coordinates[1];
            }
            else
            {
                return null;
            }

            if (isComputer)
            {
                xPosTemp = this.IncrementChar(xPos, 0);
                yPosTemp = this.IncrementChar(yPos, -1);

                destination = this.IncrementChar(xPosTemp, 0).ToString() +
                              this.IncrementChar(yPosTemp, 0).ToString();

                while (this.ValidateMove(currentPosition, destination, isComputer))
                {
                    moves.Add(currentPosition + destination);

                    xPosTemp = this.IncrementChar(xPosTemp, 0);
                    yPosTemp = this.IncrementChar(yPosTemp, -1);

                    destination = this.IncrementChar(xPosTemp, 0).ToString() +
                                  this.IncrementChar(yPosTemp, 0).ToString();
                }

                xPosTemp = this.IncrementChar(xPos, 1);
                yPosTemp = this.IncrementChar(yPos, 0);

                destination = this.IncrementChar(xPosTemp, 0).ToString() +
                              this.IncrementChar(yPosTemp, 0).ToString();

                while (this.ValidateMove(currentPosition, destination, isComputer))
                {
                    if (this.CanAttack(destination, isComputer))
                    {
                        moves.Add(currentPosition + destination);
                    }

                    xPosTemp = this.IncrementChar(xPosTemp, 1);
                    yPosTemp = this.IncrementChar(yPosTemp, 0);

                    destination = this.IncrementChar(xPosTemp, 0).ToString() +
                                  this.IncrementChar(yPosTemp, 0).ToString();
                }

                xPosTemp = this.IncrementChar(xPos, -1);
                yPosTemp = this.IncrementChar(yPos, 0);

                destination = this.IncrementChar(xPosTemp, 0).ToString() +
                              this.IncrementChar(yPosTemp, 0).ToString();

                while (this.ValidateMove(currentPosition, destination, isComputer))
                {
                    if (this.CanAttack(destination, isComputer))
                    {
                        moves.Add(currentPosition + destination);
                    }

                    xPosTemp = this.IncrementChar(xPosTemp, -1);
                    yPosTemp = this.IncrementChar(yPosTemp, 0);

                    destination = this.IncrementChar(xPosTemp, 0).ToString() +
                                  this.IncrementChar(yPosTemp, 0).ToString();
                }
            }
            else
            {
                xPosTemp = this.IncrementChar(xPos, 0);
                yPosTemp = this.IncrementChar(yPos, 1);

                destination = this.IncrementChar(xPosTemp, 0).ToString() +
                              this.IncrementChar(yPosTemp, 0).ToString();

                while (this.ValidateMove(currentPosition, destination, isComputer))
                {
                    moves.Add(currentPosition + destination);

                    xPosTemp = this.IncrementChar(xPosTemp, 0);
                    yPosTemp = this.IncrementChar(yPosTemp, 1);

                    destination = this.IncrementChar(xPosTemp, 0).ToString() +
                                  this.IncrementChar(yPosTemp, 0).ToString();
                }

                xPosTemp = this.IncrementChar(xPos, 1);
                yPosTemp = this.IncrementChar(yPos, 0);

                destination = this.IncrementChar(xPosTemp, 0).ToString() +
                              this.IncrementChar(yPosTemp, 0).ToString();

                while (this.ValidateMove(currentPosition, destination, isComputer))
                {
                    if (this.CanAttack(destination, isComputer))
                    {
                        moves.Add(currentPosition + destination);
                    }

                    xPosTemp = this.IncrementChar(xPosTemp, 1);
                    yPosTemp = this.IncrementChar(yPosTemp, 0);

                    destination = this.IncrementChar(xPosTemp, 0).ToString() +
                                  this.IncrementChar(yPosTemp, 0).ToString();
                }

                xPosTemp = this.IncrementChar(xPos, -1);
                yPosTemp = this.IncrementChar(yPos, 0);

                destination = this.IncrementChar(xPosTemp, 0).ToString() +
                              this.IncrementChar(yPosTemp, 0).ToString();

                while (this.ValidateMove(currentPosition, destination, isComputer))
                {
                    if (this.CanAttack(destination, isComputer))
                    {
                        moves.Add(currentPosition + destination);
                    }

                    xPosTemp = this.IncrementChar(xPosTemp, -1);
                    yPosTemp = this.IncrementChar(yPosTemp, 0);

                    destination = this.IncrementChar(xPosTemp, 0).ToString() +
                                  this.IncrementChar(yPosTemp, 0).ToString();
                }
            }

            return moves;
        }

        private List<string> NinjaMoves(string currentPosition, bool isComputer)
        {
            List<string> moves = new List<string>();
            string destination = string.Empty;
            char[] coordinates = currentPosition.ToCharArray();
            char xPos = ' ';
            char yPos = ' ';
            char xPosTemp = ' ';
            char yPosTemp = ' ';

            if (coordinates.Length == 2)
            {
                xPos = coordinates[0];
                yPos = coordinates[1];
            }
            else
            {
                return null;
            }

            if (isComputer)
            {
                xPosTemp = this.IncrementChar(xPos, 1);
                yPosTemp = this.IncrementChar(yPos, -1);

                destination = this.IncrementChar(xPosTemp, 0).ToString() +
                              this.IncrementChar(yPosTemp, 0).ToString();

                while (this.ValidateMove(currentPosition, destination, isComputer))
                {
                    moves.Add(currentPosition + destination);

                    xPosTemp = this.IncrementChar(xPosTemp, 1);
                    yPosTemp = this.IncrementChar(yPosTemp, -1);

                    destination = this.IncrementChar(xPosTemp, 0).ToString() +
                                  this.IncrementChar(yPosTemp, 0).ToString();
                }

                xPosTemp = this.IncrementChar(xPos, -1);
                yPosTemp = this.IncrementChar(yPos, -1);

                destination = this.IncrementChar(xPosTemp, 0).ToString() +
                              this.IncrementChar(yPosTemp, 0).ToString();

                while (this.ValidateMove(currentPosition, destination, isComputer))
                {
                    moves.Add(currentPosition + destination);

                    xPosTemp = this.IncrementChar(xPosTemp, -1);
                    yPosTemp = this.IncrementChar(yPosTemp, -1);

                    destination = this.IncrementChar(xPosTemp, 0).ToString() +
                                  this.IncrementChar(yPosTemp, 0).ToString();
                }

                xPosTemp = this.IncrementChar(xPos, -1);
                yPosTemp = this.IncrementChar(yPos, 1);

                destination = this.IncrementChar(xPosTemp, 0).ToString() +
                              this.IncrementChar(yPosTemp, 0).ToString();

                while (this.ValidateMove(currentPosition, destination, isComputer))
                {
                    if (this.CanAttack(destination, isComputer))
                    {
                        moves.Add(currentPosition + destination);
                    }

                    xPosTemp = this.IncrementChar(xPosTemp, -1);
                    yPosTemp = this.IncrementChar(yPosTemp, 1);

                    destination = this.IncrementChar(xPosTemp, 0).ToString() +
                                  this.IncrementChar(yPosTemp, 0).ToString();
                }

                xPosTemp = this.IncrementChar(xPos, 1);
                yPosTemp = this.IncrementChar(yPos, 1);

                destination = this.IncrementChar(xPosTemp, 0).ToString() +
                              this.IncrementChar(yPosTemp, 0).ToString();

                while (this.ValidateMove(currentPosition, destination, isComputer))
                {
                    if (this.CanAttack(destination, isComputer))
                    {
                        moves.Add(currentPosition + destination);
                    }

                    xPosTemp = this.IncrementChar(xPosTemp, 1);
                    yPosTemp = this.IncrementChar(yPosTemp, 1);

                    destination = this.IncrementChar(xPosTemp, 0).ToString() +
                                  this.IncrementChar(yPosTemp, 0).ToString();
                }
            }
            else
            {
                xPosTemp = this.IncrementChar(xPos, 1);
                yPosTemp = this.IncrementChar(yPos, 1);

                destination = this.IncrementChar(xPosTemp, 0).ToString() +
                              this.IncrementChar(yPosTemp, 0).ToString();

                while (this.ValidateMove(currentPosition, destination, isComputer))
                {
                    moves.Add(currentPosition + destination);

                    xPosTemp = this.IncrementChar(xPosTemp, 1);
                    yPosTemp = this.IncrementChar(yPosTemp, 1);

                    destination = this.IncrementChar(xPosTemp, 0).ToString() +
                                  this.IncrementChar(yPosTemp, 0).ToString();
                }

                xPosTemp = this.IncrementChar(xPos, -1);
                yPosTemp = this.IncrementChar(yPos, 1);

                destination = this.IncrementChar(xPosTemp, 0).ToString() +
                              this.IncrementChar(yPosTemp, 0).ToString();

                while (this.ValidateMove(currentPosition, destination, isComputer))
                {
                    moves.Add(currentPosition + destination);

                    xPosTemp = this.IncrementChar(xPosTemp, -1);
                    yPosTemp = this.IncrementChar(yPosTemp, 1);

                    destination = this.IncrementChar(xPosTemp, 0).ToString() +
                                  this.IncrementChar(yPosTemp, 0).ToString();
                }

                xPosTemp = this.IncrementChar(xPos, -1);
                yPosTemp = this.IncrementChar(yPos, -1);

                destination = this.IncrementChar(xPosTemp, 0).ToString() +
                              this.IncrementChar(yPosTemp, 0).ToString();

                while (this.ValidateMove(currentPosition, destination, isComputer))
                {
                    if (this.CanAttack(destination, isComputer))
                    {
                        moves.Add(currentPosition + destination);
                    }

                    xPosTemp = this.IncrementChar(xPosTemp, -1);
                    yPosTemp = this.IncrementChar(yPosTemp, -1);

                    destination = this.IncrementChar(xPosTemp, 0).ToString() +
                                  this.IncrementChar(yPosTemp, 0).ToString();
                }

                xPosTemp = this.IncrementChar(xPos, 1);
                yPosTemp = this.IncrementChar(yPos, -1);

                destination = this.IncrementChar(xPosTemp, 0).ToString() +
                              this.IncrementChar(yPosTemp, 0).ToString();

                while (this.ValidateMove(currentPosition, destination, isComputer))
                {
                    if (this.CanAttack(destination, isComputer))
                    {
                        moves.Add(currentPosition + destination);
                    }

                    xPosTemp = this.IncrementChar(xPosTemp, 1);
                    yPosTemp = this.IncrementChar(yPosTemp, -1);

                    destination = this.IncrementChar(xPosTemp, 0).ToString() +
                                  this.IncrementChar(yPosTemp, 0).ToString();
                }
            }

            return moves;
        }

        private void MovePiece(string move, bool retract, bool isComputer)
        {
            Dictionary<string, string> pieces;
            string destination = move.Substring(2, 2);
            char[] coordinates = destination.ToCharArray();
            char x = coordinates[0];
            char y = coordinates[1];
            string current = move.Substring(0, 2);
            char[] currCoord = current.ToCharArray();
            char currX = currCoord[0];
            char currY = currCoord[1];

            if (isComputer)
                pieces = this.compPieces;
            else
                pieces = this.humanPieces;

            if (retract)
            {
                string piece = pieces[destination];
                pieces.Remove(destination);
                pieces.Add(current, piece);
            }
            else
            {
                string piece = pieces[current];
                pieces.Remove(current);
                pieces.Add(destination, piece);
            }
        }

        private char IncrementChar(char c, int inc)
        {
            return Convert.ToChar(Convert.ToInt32(c + inc));
        }
        #endregion
    }
}
