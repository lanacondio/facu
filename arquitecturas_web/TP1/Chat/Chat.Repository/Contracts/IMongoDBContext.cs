using MongoDB.Driver;

namespace Chat.Repository.Contracts
{
    public interface IMongoDBContext
    {        
        IMongoDatabase Database { get; }
    }
}
