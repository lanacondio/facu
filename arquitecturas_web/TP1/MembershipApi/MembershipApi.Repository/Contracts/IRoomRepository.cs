using MembershipApi.DomainModel;
using System;
using System.Collections.Generic;
using System.Text;

namespace MembershipApi.Repository.Contracts
{
    public interface IRoomRepository
    {
        IList<Room> GetAll();

        Room GetById(Guid id);
        
        void Update(Room entity);

        void Delete(Guid id);
    }
}
