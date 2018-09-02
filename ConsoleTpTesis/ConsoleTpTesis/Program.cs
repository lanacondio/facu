﻿using ConsoleTpMetaheuristica.Services;
using ConsoleTpTesis.Services;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleTpMetaheuristica
{
    class Program
    {
        static void Main(string[] args)
        {
            
            var path = args[0];
            var lastBestResult = 0;
            if (args.Length > 1)
            {
                lastBestResult = int.Parse(args[1]);
            }
            try
            {
                var environment = ParseService.ParseInput(path);
                DijkstraShortestRouteService.CalculateShortestRoute(environment.Graph);
                GraspService GraspService = new GraspService();
                GraspService.Run(environment);                                                            
                
            }
            catch (Exception ex)
            {
                Console.Write(ex.Message);
            }
            
        }
    }
}
