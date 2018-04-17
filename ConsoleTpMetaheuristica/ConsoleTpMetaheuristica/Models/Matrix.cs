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

        public Matrix Clone()
        {
            var result = new Matrix();
            foreach (var row in this.Rows)
            {
                var nrow = new List<int>();
                foreach (var num in row)
                {
                    nrow.Add(num);
                }
                result.Rows.Add(nrow);
            }
            return result;
        }

        public bool IsBetter(Matrix other)
        {
            var fValue = this.Evaluate();
            var sValue = other.Evaluate();
            return fValue > sValue;
        }

        public int Evaluate()
        {
            var matrix = this;
            //ver si hay q sumar la diagonal
            int sum = 0;
            for (int j = 0; j < matrix.Rows.Count; j++)
            {
                for (int i = 0; i < j; i++)
                {
                    sum += matrix.Rows[i][j];
                }
            }
            return sum;
        }

        public Matrix Permute( int sourceIndex, int destIndex)
        {            
            this.PermuteRows(sourceIndex, destIndex);
            this.PermuteColumns(sourceIndex, destIndex);
            return this;
        }

        public Matrix PermuteRows(int sourceIndex, int destIndex)
        {
            var originalRow = this.Rows[destIndex];
            this.Rows[destIndex] = this.Rows[sourceIndex];
            this.Rows[sourceIndex] = originalRow;
            return this;
        }

        public Matrix PermuteColumns(int sourceIndex, int destIndex)
        {
            for (int i = 0; i < this.Rows.Count; i++)
            {
                var aux = this.Rows[i][destIndex];
                this.Rows[i][destIndex] = this.Rows[i][sourceIndex];
                this.Rows[i][sourceIndex] = aux;

            }

            return this;
        }


    }
}
