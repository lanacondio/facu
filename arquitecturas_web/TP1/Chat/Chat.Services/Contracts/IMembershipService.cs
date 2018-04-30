using Chat.DomainModel;
using System;
using System.Collections.Generic;
using System.Text;

namespace Chat.Services.Contracts
{
    public interface IMembershipService
    {
        User Login(string name, string password);

        void LogOut(string token);

        User GetUserByName(string userName);
        User Create(string name, string password, int age, string city);
    }
}
