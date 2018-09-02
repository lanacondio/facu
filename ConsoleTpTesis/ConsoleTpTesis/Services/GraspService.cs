﻿using ConsoleTpTesis.Models;
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
        private static bool Finished { get; set; }
        private static Random r = new Random();
        public void Run(GraphEnvironment environment)
        {
            var truksToRoad = environment.Trucks;
            while (!Finished)
            {
                //trucksToRoad
            }

            var maxSeeds = int.Parse(ConfigurationManager.AppSettings["MaxSeeds"]);
            
            //var seeds = new  List<Matrix>();
            //var results = new List<Matrix>();

            //for (int i = 0; i < maxSeeds; i++)
            //{
            //    var seed = this.MakeSeed(matrix);                
            //    seeds.Add(seed);
                
            //}

            //Parallel.ForEach(seeds, (actualSeed) =>
            //{
            //    var worker = new LocalSearchWorker(actualSeed);
            //    worker.Run();
            //    results.Add(worker.resultSeed);
            //});


            //result = this.GetBest(results);
            //return result;
        }

        //public Matrix GetBest(List<Matrix> results)
        //{
        //    var result = results[0];

        //    for (int i = 1; i < results.Count; i++)
        //    {
        //        if (results[i].IsBetter(result)) result = results[i];
        //    }

        //    return result;
        //}

        //public Matrix MakeSeed(Matrix matrix)
        //{
        //    var result = matrix.Clone();
        //    for (int i = result.Rows.Count - 1; i > 0; i--)
        //    {               
        //        var bestColumnIndex = GetBestColumnIndexBetweenWithRandomPercentage(0, i, result);
        //        result.Permute(bestColumnIndex, i);
        //    }

        //    return result;

        //}

        //public int GetBestColumnIndexBetween(int lowerIndex, int upperIndex, Matrix matrix)
        //{
        //    var resultIndex = lowerIndex;
        //    var resultValue = this.EvaluateColumn(lowerIndex, upperIndex, matrix, lowerIndex);
        //    for (int i = lowerIndex; i < upperIndex; i++)
        //    {
        //        var columnValue = this.EvaluateColumn(lowerIndex, upperIndex, matrix, i);
        //        if (columnValue > resultValue) resultIndex = i;
        //    }
        //    return resultIndex;
        //}

        //public int GetBestColumnIndexBetweenWithRandomPercentage(int lowerIndex, int upperIndex, Matrix matrix)
        //{

        //    var randomPercentage = int.Parse(ConfigurationManager.AppSettings["RandomPercentage"]);
        //    var resultLenght = int.Parse(Math.Round((decimal)(matrix.Rows.Count * randomPercentage / 100)).ToString());
        //    var possibleResults = new Dictionary<int, List<int>>();
            
        //    var resultValue = this.EvaluateColumn(lowerIndex, upperIndex, matrix, lowerIndex);
        //    var actualLenghtResults = 1;

        //    possibleResults.Add(resultValue, new List<int>() { lowerIndex });

        //    for (int i = lowerIndex; i < upperIndex; i++)
        //    {
        //        var columnValue = this.EvaluateColumn(lowerIndex, upperIndex, matrix, i);
        //        if (actualLenghtResults < resultLenght)
        //        {
        //            if (possibleResults.ContainsKey(columnValue))
        //            {
        //                possibleResults[columnValue].Add(i);
        //            }
        //            else
        //            {
        //                possibleResults.Add(columnValue, new List<int>() { i });
        //            }
        //            actualLenghtResults++;
        //        }
        //        else
        //        {
        //            if (columnValue > possibleResults.Keys.Min())
        //            {
        //                possibleResults.Remove(possibleResults.Keys.Min());
        //                if (possibleResults.ContainsKey(columnValue))
        //                {
        //                    possibleResults[columnValue].Add(i);
        //                }
        //                else
        //                {
        //                    possibleResults.Add(columnValue, new List<int>() { i });
        //                }
                                                
        //            }
        //        }
        //    }

            
        //    int resultKeyIndex = r.Next(0, possibleResults.Keys.Count);
        //    var key = possibleResults.Keys.ToArray()[resultKeyIndex];
        //    var resultIndex = possibleResults[key];
        //    var result = 0;
        //    if (resultIndex.Count > 1)
        //    {
            
        //        int xIndex = r.Next(0, resultIndex.Count);
        //        result = resultIndex[xIndex];
        //    }
        //    else
        //    {
        //        result = resultIndex[0];
        //    }
        //    return result;
        //}

        //public int EvaluateColumn(int lowerIndex, int upperIndex, Matrix matrix, int columnIndex)
        //{
        //    var sum = 0;
        //    for (int i = lowerIndex; i < upperIndex; i++)
        //    {
        //        sum += matrix.Rows[i][columnIndex];
        //    }
        //    return sum;
        //}

        private bool CanVisit(Truck truck, Graph graph, Node dest)
        {
            var origin = truck.Travel.Last();

            var fstNode = origin.Id < dest.Id ? origin : dest;
            var sndNode = origin.Id == fstNode.Id ? dest : origin;

            var arc = graph.Arcs.Where(x => x.first.Id == fstNode.Id && x.second.Id == sndNode.Id).FirstOrDefault();
            return arc != null && arc.Cost <= truck.TimeLimit && arc.Demand <= truck.Capacity;
            
        }
        
        
    }
}
