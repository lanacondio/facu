using ConsoleTpMetaheuristica.Services;
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
                var matrix = MatrixReader.Read(path);

                GraspService GraspService = new GraspService();
                var heuristicResult = GraspService.GetResult(matrix);
                
                MatrixReader.Write(path, heuristicResult, heuristicResult.Evaluate());
            }
            catch (Exception ex)
            {
                Console.Write(ex.Message);
            }
            
        }
    }
}
