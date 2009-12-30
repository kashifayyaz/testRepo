/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kashif
 */
import java.util.List;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.TermQuery;
import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import org.apache.lucene.search.Query;
import org.hibernate.HibernateException;
import org.hibernate.search.Search;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.search.FullTextSession;

public class Main {

     public static void main(String[] args) throws Exception {

       //Set up database tables
        //HibernateUtil.droptable("drop table person");
        //HibernateUtil.setup("create table person ( id int, cname VARCHAR(20))");

         //Create SessionFactory and Session object
          SessionFactory sessions = new Configuration().configure().buildSessionFactory();
          Session session = sessions.openSession();

        //Perform life-cycle operations under a transaction
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            // Create a Person object and save it
            Person p1 = new Person();
            p1.setName("Ahmad   ");
            //session.save(p1);

            // Create another Person object and save it.
            Person p2 = new Person();
            p2.setName("Rehan");
            //session.save(p2);
/////////////////////////////////////////////indexing////////////////////////
    List<Person> notes = session.createCriteria(Person.class).list();
    FullTextSession ftsess = Search.getFullTextSession(session);

    ftsess.getTransaction().begin();
      for (Person person1 : notes) {
              ftsess.index(person1);
    }
    ftsess.getTransaction().commit();
/////////////////////////////////////////////////////////////////////////////
 // obtain the search key and define the Lucene parser

    String[] fields = new String[]{"name"};
    QueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());

    String key = javax.swing.JOptionPane.showInputDialog("Enter search key");
    if (key == null) {
      // dialog was cancelled
      System.exit(1);
    }

    key = key.trim();

    org.apache.lucene.search.Query lquery = null;
    try {
      lquery = parser.parse(key);
    } catch (Exception x) {
      x.printStackTrace();
      System.exit(1);
    }
///////////////////////////////////////////////////////////////////////////
    // create the Hibernate search query as a wrapper around
    // the Lucene query

    org.hibernate.Query
      hquery = ftsess.createFullTextQuery(lquery, Person.class);

    List<Person> results = hquery.list();  // execute the query

    System.out.println("\n# matching records: " + results.size());
    for (Person note : results) {
      System.out.println("\n" + note.getName());
    }
    // Display tables
        //HibernateUtil.checkData("select * from person");


    System.exit(0);
        } catch ( HibernateException e ) {
            if ( tx != null ) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();

        }
        
    }
}
