import com.unboundid.ldap.sdk.*;

public class UnboundIdMemLeak
{
  private static int count;

  public static void main(String[] args)
    throws Exception
  {
    if (args.length < 4) {
      System.err.println("usage: UnboundIdMemLeak <host> <port> <baseDN> <filter>");
      System.exit(-1);
    }

    for (int i = 0; i < 10; i++) {
      LDAPConnection conn = new LDAPConnection();
      conn.connect(args[0], Integer.parseInt(args[1]));
      SearchRequest request = new SearchRequest(
        args[2], SearchScope.SUB, args[3]);
      (new Searcher(conn, request)).start();
    }
  }

  static class Searcher extends Thread
  {
    private LDAPConnection conn;
    private SearchRequest request;

    public Searcher(LDAPConnection lc, SearchRequest sr)
    {
      conn = lc;
      request = sr;
    }

    public void run()
    {
      while (true) {
        try {
          Thread.sleep(10);
          LDAPEntrySource source = new LDAPEntrySource(conn, request, false);
          try {
            while (true) {
              Entry entry = source.nextEntry();
              if (entry == null) break;
            }
          } finally {
            source.close();
          }
          System.out.println("search count: " + count++);
        } catch (Exception e) {
          e.printStackTrace(); 
          break;
        }
      }
    }
  }
}
