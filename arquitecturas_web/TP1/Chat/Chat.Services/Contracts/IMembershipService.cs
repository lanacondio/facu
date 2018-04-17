using Chat.DomainModel;
using System;
using System.Collections.Generic;
using System.Text;

namespace Chat.Services.Contracts
{
    public interface IMembershipService
    {
        User Login(string name);

        void LogOut(Guid id, string token);

        User GetUserByName(string userName);
    }
}
