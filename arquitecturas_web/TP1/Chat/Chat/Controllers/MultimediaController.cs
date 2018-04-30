using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using System.IO;
using Microsoft.AspNetCore.Hosting;

namespace Chat.Controllers
{
    public class MultimediaController : Controller
    {
        private readonly IHostingEnvironment _env;

        public MultimediaController(IHostingEnvironment env)
        {
            _env = env;
        }

        [HttpPost]
        public async Task<IActionResult> Upload()
        {
            if (Request.Form.Files.Count > 0)
            {
                var file = Request.Form.Files[0];

                if (file != null && file.Length > 0)
                {
                    var fileName = Path.GetFileName(file.FileName);

                    var path = Path.Combine(_env.WebRootPath, "images", fileName);
                    
                    using (var stream = new FileStream(path, FileMode.Create))
                    {
                        await file.CopyToAsync(stream);
                    }
                    var relativePath = "/images/" + fileName;
                return Ok(new { count = Request.Form.Files.Count, Request.Form.Files[0].Length, relativePath});
                }

                return Ok();
            }

            return Ok();

        }
    }
}