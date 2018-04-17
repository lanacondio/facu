using MembershipApi.Repository.Contracts;
using MembershipApi.Services.Contracts;
using System;
using System.Collections.Generic;
using System.Text;
using MembershipApi.DomainModel;
using System.Linq;

namespace MembershipApi.Services.Implementation
{
 
    public class MembershipService : IMembershipService
    {
        public virtual IUserRepository UserRepository { get; set; }
        public virtual IRoomRepository RoomRepository { get; set; }

        public MembershipService(IUserRepository userRepository, IRoomRepository roomRepository)
        {
            this.UserRepository = userRepository;
            this.RoomRepository = roomRepository;
        }

        public User Login(string name)
        {
            var users = this.UserRepository.GetAll();

            if (users.Any(x => x.Name == name)) { throw new Exception("name already used"); }

            var user = new User()
            {
                Name = name,
                Token = Guid.NewGuid().ToString()
            };

            this.UserRepository.Insert(user);

            var generalRoom = this.RoomRepository.GetAll().Where(x => x.Name == "General").FirstOrDefault();

            generalRoom.Users.Add(user.Name);

            this.RoomRepository.Update(generalRoom);

            return user;

        }

        public void LogOut(Guid id, string token)
        {
            var user = this.UserRepository.GetById(id);

            if (user.Token != token) { throw new Exception("invalid token"); }

            this.UserRepository.Delete(id);

            var rooms = this.RoomRepository.GetAll().Where(x => x.Users.Contains(user.Name) && x.Name != "General").ToList();

            rooms.ForEach(x => this.RoomRepository.Delete(x.Id));

            var generalRoom = this.RoomRepository.GetAll().Where(x => x.Name == "General").FirstOrDefault();

            generalRoom.Users.Remove(user.Name);

            this.RoomRepository.Update(generalRoom);

        }
    }

}
