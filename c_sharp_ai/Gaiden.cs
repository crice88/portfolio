using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Ninja_Gaiden
{
    /// <summary>
    /// Main entry point for Ninja Gaiden game.
    /// </summary>
    /// <description>
    /// Colin Rice
    /// CSc 180 - Assignment 2
    /// Adversarial Board Game
    /// Rules by Dr. Gordon
    /// October 21st, 2017
    /// </description>
    class Gaiden
    {
        private Game game;

        public Gaiden()
        {
            this.game = new Game();
        }

        static void Main(string[] args)
        {
            new Gaiden();
        }
    }
}
