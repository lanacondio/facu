using Chat.DomainModel;
using Chat.Repository.Contracts;
using MongoDB.Driver;
using System.Collections.Generic;
using System;

namespace Chat.Repository.Implementations
{
    public class UserRepository : IUserRepository
    {
        public IMongoDBContext Context { get; }

        public UserRepository(IMongoDBContext context) { this.Context = context; }

        public void Delete(Guid id)
        {            
            this.Context.Database.GetCollection<User>("user").DeleteOne(x => x.Id == id);
        }


        public User GetById(Guid id)
        {
            var user = this.Context.Database.GetCollection<User>("user").Find(m => m.Id == id).FirstOrDefault();
            return user;
        }

        public User GetByToken(string token)
        {
            var user = this.Context.Database.GetCollection<User>("user").Find(m => m.Token == token).FirstOrDefault();
            return user;
        }

        public IList<User> GetAll()
        {
            var users = this.Context.Database.GetCollection<User>("user").Find(m => true).ToList();
            return users;
        }

        public Guid Insert(User entity)
        {            
            entity.Id = Guid.NewGuid();
            this.Context.Database.GetCollection<User>("user").InsertOne(entity);
            return entity.Id;
        }

        public void Update(User entity)
        {
            this.Context.Database.GetCollection<User>("user").ReplaceOne(x => x.Id == entity.Id, entity);
        }
    }
}
