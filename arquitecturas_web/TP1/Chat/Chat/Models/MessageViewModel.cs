﻿using Chat.DomainModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Chat.Models
{
    public class MessageViewModel
    {
        public string Content { get; set; }
        public string roomName { get; set; }        
    }
}
