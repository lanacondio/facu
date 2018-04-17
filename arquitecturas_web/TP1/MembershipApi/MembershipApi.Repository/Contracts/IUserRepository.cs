using MembershipApi.DomainModel;
using System;
using System.Collections.Generic;
using System.Text;

namespace MembershipApi.Repository.Contracts
{
    public interface IUserRepository
    {
        IList<User> GetAll();

        User GetById(Guid id);

        Guid Insert(User user);

        void Update(User entity);

        void Delete(Guid id);
    }
}
