using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleTpMetaheuristica.Models
{
    public class Matrix
    {
        public Matrix()
        {
            this.Rows = new List<List<int>>();
        }

        public List<List<int>> Rows { get; set; }
    }
}
