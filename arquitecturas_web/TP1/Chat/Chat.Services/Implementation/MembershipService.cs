using Chat.Repository.Contracts;
using Chat.Services.Contracts;
using System;
using System.Collections.Generic;
using System.Text;
using Chat.DomainModel;
using System.Linq;

namespace Chat.Services.Implementation
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

        public User Login(string name, string password)
        {
            var user = this.UserRepository.GetAll().Where(x => x.Name == name).FirstOrDefault();

            if(user.Password == password)
            {
                var generalRoom = this.RoomRepository.GetAll().Where(x => x.Name == "General").FirstOrDefault();

                generalRoom.Users.Add(user);

                this.RoomRepository.Update(generalRoom);

                return user;

            }
            else
            {
                throw new Exception("incorrect user or password");
            }
            
        }

        public User Create(string name, string password, int age, string city)
        {
            var users = this.UserRepository.GetAll();

            if (users.Any(x => x.Name == name)) { throw new Exception("name already used"); }

            var user = new User()
            {
                Name = name,
                Password = password,
                Age = age,
                City = city,
                Token = Guid.NewGuid().ToString()
            };

            this.UserRepository.Insert(user);            

            return user;

        }


        public void LogOut(string token)
        {
            var user = this.UserRepository.GetByToken(token);

            if (user.Token != token) { throw new Exception("invalid token"); }

            var rooms = this.RoomRepository.GetAll().Where(x => x.Users.Contains(user) && x.Name != "General").ToList();

            rooms.ForEach(x => this.RoomRepository.Delete(x.Id));

            var generalRoom = this.RoomRepository.GetAll().Where(x => x.Name == "General").FirstOrDefault();

            generalRoom.Users.Remove(user);

            this.RoomRepository.Update(generalRoom);

        }

        public User GetUserByName(string userName)
        {
            var result = this.UserRepository.GetAll().Where(x => x.Name == userName).FirstOrDefault();
            return result;
        }
    }

}
