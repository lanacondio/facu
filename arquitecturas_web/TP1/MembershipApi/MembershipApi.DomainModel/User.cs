﻿using System;

namespace MembershipApi.DomainModel
{
    public class User
    {
        public virtual Guid Id { get; set; }
        public virtual string Token { get; set; }
        public virtual string Name { get; set; }
    }
}
