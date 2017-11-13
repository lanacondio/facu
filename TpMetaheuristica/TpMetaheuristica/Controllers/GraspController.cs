using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using TpMetaheuristica.Models;
using TpMetaheuristica.Services;

namespace TpMetaheuristica.Controllers
{
    [Route("api/[controller]")]
    public class GraspController : Controller
    {

        private GraspService GraspService = new GraspService();
        // POST api/values
        [HttpPost]
        public ObjectResult Post([FromBody]Matrix value)
        {
            var result = this.GraspService.GetResult(value);
            return Ok(result);
        }

      
    }
}
