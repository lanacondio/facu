﻿using AutoMapper;
using MembershipApi.DomainModel;
using MembershipApi.Repository.Contracts;
using MongoDB.Driver;
using System.Collections.Generic;
using System;

namespace MembershipApi.Repository.Implementations
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
            return Mapper.Map<User>(user);
        }

        public IList<User> GetAll()
        {
            var users = this.Context.Database.GetCollection<User>("user").Find(m => true).ToList();
            return Mapper.Map<IList<User>>(users);
        }

        public Guid Insert(User user)
        {
            var entity = Mapper.Map<User>(user);
            entity.Id = Guid.NewGuid();
            this.Context.Database.GetCollection<User>("user").InsertOne(entity);
            return entity.Id;
        }

        public void Update(User user)
        {
            var entity = Mapper.Map<User>(user);
            this.Context.Database.GetCollection<User>("user").ReplaceOne(x => x.Id == entity.Id, entity);
        }
    }
}
