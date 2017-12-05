using ConsoleTpMetaheuristica.Models;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleTpMetaheuristica.Services
{
    public class GraspService
    {
        public Matrix GetResult(Matrix matrix)
        {
            var result = matrix;

            //ver condicion de parada
            //var stopCondition = false;
            
            var maxSeeds = int.Parse(ConfigurationManager.AppSettings["MaxSeeds"]);
            var maxIterations = int.Parse(ConfigurationManager.AppSettings["MaxIterations"]);


            for (int i = 0; i < maxSeeds; i++)
            {
                var seed = this.MakeSeed(matrix);

                if (IsBetter(seed, result)) result = seed;

                for (int j = 0; j < maxIterations; j++)
                {
                    var localSolution = this.MakeOtherSolution(seed);

                    if (IsBetter(localSolution, result)) result = localSolution;

                }

            }


            return result;
        }

        public Matrix MakeSeed(Matrix matrix)
        {
            var result = matrix;

            for (int i = result.Rows.Count - 1; i > 0; i--)
            {
                //var bestColumnIndex = GetBestColumnIndexBetween(0, i, result);
                var bestColumnIndex = GetBestColumnIndexBetweenWithRandomPercentage(0, i, result);
                this.Permute(result, bestColumnIndex, i);
            }

            return result;

        }

        public int GetBestColumnIndexBetween(int lowerIndex, int upperIndex, Matrix matrix)
        {
            var resultIndex = lowerIndex;
            var resultValue = this.EvaluateColumn(lowerIndex, upperIndex, matrix, lowerIndex);
            for (int i = lowerIndex; i < upperIndex; i++)
            {
                var columnValue = this.EvaluateColumn(lowerIndex, upperIndex, matrix, i);
                if (columnValue > resultValue) resultIndex = i;
            }
            return resultIndex;
        }

        public int GetBestColumnIndexBetweenWithRandomPercentage(int lowerIndex, int upperIndex, Matrix matrix)
        {

            var randomPercentage = int.Parse(ConfigurationManager.AppSettings["RandomPercentage"]);
            var resultLenght = int.Parse(Math.Round((decimal)(matrix.Rows.Count * randomPercentage / 100)).ToString());
            var possibleResults = new Dictionary<int, List<int>>();
            
            var resultValue = this.EvaluateColumn(lowerIndex, upperIndex, matrix, lowerIndex);
            var actualLenghtResults = 1;

            possibleResults.Add(resultValue, new List<int>() { lowerIndex });

            for (int i = lowerIndex; i < upperIndex; i++)
            {
                var columnValue = this.EvaluateColumn(lowerIndex, upperIndex, matrix, i);
                if (actualLenghtResults < resultLenght)
                {
                    if (possibleResults.ContainsKey(columnValue))
                    {
                        possibleResults[columnValue].Add(i);
                    }
                    else
                    {
                        possibleResults.Add(columnValue, new List<int>() { i });
                    }
                    actualLenghtResults++;
                }
                else
                {
                    if (columnValue > possibleResults.Keys.Min())
                    {
                        possibleResults.Remove(possibleResults.Keys.Min());
                        if (possibleResults.ContainsKey(columnValue))
                        {
                            possibleResults[columnValue].Add(i);
                        }
                        else
                        {
                            possibleResults.Add(columnValue, new List<int>() { i });
                        }
                        
                        
                    }
                }
            }

            Random r = new Random();
            int resultKeyIndex = r.Next(0, possibleResults.Keys.Count);
            var key = possibleResults.Keys.ToArray()[resultKeyIndex];
            var resultIndex = possibleResults[key];
            var result = 0;
            if (resultIndex.Count > 1)
            {
                Random x = new Random();
                int xIndex = r.Next(0, resultIndex.Count);
                result = resultIndex[xIndex];
            }
            else
            {
                result = resultIndex[0];
            }
            return result;
        }

        public int EvaluateColumn(int lowerIndex, int upperIndex, Matrix matrix, int columnIndex)
        {
            var sum = 0;
            for (int i = lowerIndex; i < upperIndex; i++)
            {
                sum += matrix.Rows[i][columnIndex];
            }
            return sum;
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
            for (int i = 0; i < matrix.Rows.Count; i++)
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
                    if (i < j)
                    {
                        sum += matrix.Rows[i][j];
                    }
                }
            }
            return sum;
        }

    }
}
