# order-system-example

Run the application from the command line

   mvn spring-boot:run
   
You should get some dialog like: 
   
     .   ____          _            __ _ _
     /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
    ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
     \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
      '  |____| .__|_| |_|_| |_\__, | / / / /
     =========|_|==============|___/=/_/_/_/
     :: Spring Boot ::        (v2.3.5.RELEASE)
    
    Options:
      BUY customer-name item-name quantity
      ADD item-name quantity
      REG customer-name email
    2020-11-05 15:54:13.772 ERROR 24936 --- [           main] o.a.zookeeper.server.ZooKeeperServer     : ZKShutdownHandler is not registered, so ZooKeeper server won't take any actio
    n on ERROR or SHUTDOWN server state changes
    Start entering requests now..

At the end of that, you can start entering requests.

### Request types

*BUY*

This is a request to buy an item (currently, only apples and oranges). You will need to enter three values like so:

    BUY <your name> <item name> <quantity>
    
examples:

    BUY john apple 2
    BUY henry orange 5
    
*REG*

This registers the customer's email address to receive messages
 
    REG <name> <email>
 
examples
 
    REG john john@test.com
    REG henry hgwells@scifi.net
     
*ADD*
 
An executive function to add items to the inventory
 
    ADD <item> <quantity>
    
Examples
 
    ADD orange 12
    ADD apple 100
    
## Terminating

Just hit `Ctrl+C`

Please note, I didn't have a lot of time for safety checks. 
Parsing errors or too many spaces between words will cause 
application failures.

## TODO

Need a way to introduce new item types with prices. Likely
just add another topic for something this simple. In a real 
system we'd have controllers, and inventory/price management 
would be different services/controllers.

There's also no state saved. And I broke most of my unit 
tests when I added Kafka (should have read all the way through
the test questions), and didn't have time to fix within 90 
minutes.

