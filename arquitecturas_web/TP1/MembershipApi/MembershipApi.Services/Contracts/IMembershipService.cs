using MembershipApi.DomainModel;
using System;
using System.Collections.Generic;
using System.Text;

namespace MembershipApi.Services.Contracts
{
    public interface IMembershipService
    {
        User Login(string name);

        void LogOut(Guid id, string token);

    }
}
