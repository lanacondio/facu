using Chat.DomainModel;
using Chat.Repository.Contracts;
using MongoDB.Driver;
using System.Collections.Generic;
using System;

namespace Chat.Repository.Implementations
{
    public class RoomRepository : IRoomRepository
    {
        public IMongoDBContext Context { get; }

        public RoomRepository(IMongoDBContext context) { this.Context = context; }

        public void Delete(Guid id)
        {            
            this.Context.Database.GetCollection<Room>("room").DeleteOne(x => x.Id == id);
        }


        public Room GetById(Guid id)
        {
            var Room = this.Context.Database.GetCollection<Room>("room").Find(m => m.Id == id).FirstOrDefault();
            return Room;
        }

        public IList<Room> GetAll()
        {
            var Rooms = this.Context.Database.GetCollection<Room>("room").Find(m => true).ToList();
            return Rooms;
        }

        public Guid Insert(Room room)
        {
            room.Id = Guid.NewGuid();
            this.Context.Database.GetCollection<Room>("room").InsertOne(room);
            return room.Id;
        }

        public void Update(Room room)
        {
            //this.Context.Database.GetCollection<Room>("room").DeleteOne(x => x.Id == room.Id);
            //this.Context.Database.GetCollection<Room>("room").InsertOne(room);

            this.Context.Database.GetCollection<Room>("room").ReplaceOne(x => x.Id == room.Id, room);
        }
    }
}
