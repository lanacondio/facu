using MongoDB.Driver;

namespace MembershipApi.Repository.Contracts
{
    public interface IMongoDBContext
    {        
        IMongoDatabase Database { get; }
    }
}
