using ConsoleTpMetaheuristica.Models;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleTpMetaheuristica
{
    public static class MatrixReader
    {
        public static Matrix Read(string path)
        {
            var matrix = new Matrix();
            Console.Write("loading file from: " + path);
            using (var reader = new StreamReader(path))
            {
                var rowsCount = int.Parse(reader.ReadLine());

                while (rowsCount > 0)
                {                   
                    var row = reader.ReadLine().Trim().Split(' ').Select(int.Parse).ToList();
                    matrix.Rows.Add(row);
                    rowsCount--;
                }

                // process line here
            }
            return matrix;
        }

        public static void Write(string path, Matrix result, int resultValue)
        {
            var saveMatrix = bool.Parse(ConfigurationManager.AppSettings["SaveMatrix"]);
            using (StreamWriter w = File.AppendText(Path.Combine(GetParentDirectory(path,1), "output.txt")))
            {
                w.Write(path + ";");
                w.Write(DateTime.Now +";");
                w.Write(result.Rows.Count + ";");
                if (saveMatrix)
                {
                    foreach (var row in result.Rows)
                    {
                        w.Write("[" + string.Join(",", row.ToArray()) + "];");
                    }
                }
                
                w.Write(resultValue.ToString()+";");
                w.WriteLine(" ");
            }
                        

        }

        public static string GetParentDirectory(string path, int parentCount)
        {
            if (string.IsNullOrEmpty(path) || parentCount < 1)
                return path;

            string parent = System.IO.Path.GetDirectoryName(path);

            if (--parentCount > 0)
                return GetParentDirectory(parent, parentCount);

            return parent;
        }
    }
}
