using Microsoft.AspNetCore.Http;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Chat.Models
{
    public class VoicePassage
    {
        public string Title { get; set; }
        public string FileName { get; set; }
        public IFormFile Recording { get; set; }
    }
}
