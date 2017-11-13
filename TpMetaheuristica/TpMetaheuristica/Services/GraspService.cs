using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using TpMetaheuristica.Models;

namespace TpMetaheuristica.Services
{
    public class GraspService
    {
        public Matrix GetResult(Matrix matrix)
        {
            var result = matrix;

            //ver condicion de parada
            //var stopCondition = false;
            int i = 0;
            while (i<matrix.Rows.Count * 100)
            {
                var localSolution = this.MakeOtherSolution(matrix);

                if (IsBetter(localSolution, result))
                {
                    result = localSolution;
                }

                i++;
            }

            return result;
        }

        public Matrix MakeOtherSolution(Matrix matrix)
        {
            Random r = new Random();
            int sourceIndex = r.Next(0, matrix.Rows.Count); 
            var destIndex = r.Next(0, matrix.Rows.Count);
            var result = this.Permute(matrix, sourceIndex, destIndex);
            return result;

        }
        

        public Matrix Permute(Matrix matrix, int sourceIndex, int destIndex)
        {
            var result = matrix;
            this.PermuteRows(result, sourceIndex, destIndex);
            this.PermuteColumns(result, sourceIndex, destIndex);
            return result;
        }

        public Matrix PermuteRows(Matrix matrix, int sourceIndex, int destIndex)
        {
            var originalRow = matrix.Rows[destIndex];
            matrix.Rows[destIndex] = matrix.Rows[sourceIndex];
            matrix.Rows[sourceIndex] = originalRow;
            return matrix;
        }

        public Matrix PermuteColumns(Matrix matrix, int sourceIndex, int destIndex)
        {
            for(int i= 0; i<matrix.Rows.Count; i++)
            {
                var aux = matrix.Rows[i][destIndex];
                matrix.Rows[i][destIndex] = matrix.Rows[i][sourceIndex];
                matrix.Rows[i][sourceIndex] = aux;

            }

            return matrix;
        }


        
        public bool IsBetter(Matrix first, Matrix second)
        {   
            var fValue = this.Evaluate(first);
            var sValue = this.Evaluate(second);
            return fValue > sValue;
            
        }

        public int Evaluate(Matrix matrix)
        { 
            //ver si hay q sumar la diagonal
            int sum = 0;
            for (int j = 0; j < matrix.Rows.Count; j++)
            {
                for (int i = 0; i < matrix.Rows.Count; i++)
                {
                    if (i <= j)
                    {
                        sum += matrix.Rows[i][j];
                    }
                }
            }
            return sum;
        }

    }
}
