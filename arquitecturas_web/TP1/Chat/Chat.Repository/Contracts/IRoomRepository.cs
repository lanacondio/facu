using Chat.DomainModel;
using System;
using System.Collections.Generic;
using System.Text;

namespace Chat.Repository.Contracts
{
    public interface IRoomRepository
    {
        IList<Room> GetAll();

        Room GetById(Guid id);

        Guid Insert(Room entity);

        void Update(Room entity);

        void Delete(Guid id);
    }
}
