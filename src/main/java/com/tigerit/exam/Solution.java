package com.tigerit.exam;

import static com.tigerit.exam.IO.*;

import java.util.*; 

/**
 * All of your application logic should be placed inside this class.
 * Remember we will load your application from our custom container.
 * You may add private method inside this class but, make sure your
 * application's execution points start from inside run method.
 */ 

public class Solution implements Runnable 
{
    ArrayList <Integer> vint;
    ArrayList <String> vstr;
    String keep;

    Integer T, n, q;
    String[] name = new String[12]; ///table name 
    Integer[] cl = new Integer[12]; ///number of columns in each table
    Integer[] dt = new Integer[12]; ///numder of rows in each table
    String[][] col = new String[12][105]; ///column names for each table
    Integer[][][] table = new Integer[12][105][105]; ///data for each table
    
    Integer id1, id2; ///id of each query table
    Integer c1, c2; ///match column of each query table
    Integer fc; ///answer column count
    Integer[] lst = new Integer[205]; ///answer column list
    
    ArrayList < ArrayList <Integer> > ara;

    public void get_str()
    {
        String str = readLine();
        keep = str;
        ArrayList<String> ret = new ArrayList<String>();
        String cur = new String();
        for(Integer i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if( c == ' ' )
            {
                ret.add(cur);
                cur = new String();
                continue;
            }
            cur = cur + c;
        }     
        ret.add(cur);
        vstr = ret;
        //for(Integer i = 0; i < vstr.size(); i++) printLine( vstr.get(i) );
    }
    public void get_int()
    {
        String str = readLine();
        keep = str;
        ArrayList <Integer> ret = new ArrayList <Integer> ();
        Integer cur = 0;
        for(Integer i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if( c == ' ' )
            {
                ret.add(cur);
                cur = 0;
                continue;
            }
            cur = cur * 10 + (c - 48);
        }     
        ret.add(cur);
        vint = ret;
        //for(Integer i = 0; i < vint.size(); i++) printLine( vint.get(i) );
    }
    String get_prefix(String str)
    {
        String ret = new String(); 
        for(Integer i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if( c == '.' ) break;
            ret = ret + c;
        }     
        return ret;
    }
    String get_column(String str)
    {
        String ret = new String();
        Integer st = 0;
        for(Integer i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if( c == '.' ) 
            {
                st = 1;
                continue;
            }
            if( st == 0 ) continue;
            if( c == ',' ) break;
            ret = ret + c;
        }     
        return ret;
    }

    Integer get_column_id(Integer t, String str)
    {
        for(Integer j = 0; j < cl[t]; j++) if( col[t][j].equals( str ) ) return j;
        return -1;
    }

    void get_question()
    {
        String sn1 = new String(), sn2 = new String();

        get_str();
        String tmp = keep;
        ArrayList <String> al = vstr;
        
        get_str();
        if( vstr.size() > 2 ) sn1 = vstr.get(2);
        for(Integer i = 0; i < n; i++) if( name[i].equals( vstr.get(1) ) ) id1 = i;

        get_str();
        if( vstr.size() > 2 ) sn2 = vstr.get(2);
        for(Integer i = 0; i < n; i++) if( name[i].equals( vstr.get(1) ) ) id2 = i;

        get_str();
        String clm;
        clm = get_column( vstr.get(1) );
        for(Integer j = 0; j < cl[id1]; j++) if( col[id1][j].equals(clm) ) c1 = j;
        clm = get_column( vstr.get(3) );
        for(Integer j = 0; j < cl[id2]; j++) if( col[id2][j].equals(clm) ) c2 = j;
        
        if( tmp.charAt(7) == '*' )
        {
            fc = 0;
            for(Integer j = 0; j < cl[id1]; j++) lst[ fc++ ] = +( j + 1 );
            for(Integer j = 0; j < cl[id2]; j++) lst[ fc++ ] = -( j + 1 ); 
        }
        else 
        {
            fc = 0;
            for(Integer i = 1; i < al.size(); i++)
            {   
                String sn = get_prefix( al.get(i) );
                clm = get_column( al.get(i) );
                Integer id;
                if( sn.equals( sn1 ) ) id = id1;
                else id = id2;
                Integer c = get_column_id(id, clm);
                if( id == id1 ) lst[ fc++ ] = +(c + 1);
                else lst[ fc++ ] = -(c + 1);
            }
        }   

        get_str();
    }
    
    void add_row(Integer r1, Integer r2)
    {
        ArrayList <Integer> cur = new ArrayList <Integer>(); 
        for(Integer i = 0; i < fc; i++)
        {
            Integer id, r, c = lst[i];
            if( c > 0 ) 
            {
                id = id1;
                r = r1;
                c--;
            }
            else 
            {
                id = id2;
                r = r2;
                c = -c;
                c--;
            }
            cur.add( table[id][r][c] ); 
        }
        ara.add( cur );
    }
    
    void show()
    {
        for(Integer i = 0; i < fc; i++)
        {
            Integer id, c = lst[i];
            if( c > 0 ) 
            {
                id = id1;
                c--;
            }
            else 
            {
                id = id2;
                c = -c;
                c--;
            }
            if( i > 0 ) System.out.print(" "); 
            System.out.print( col[id][c] ); 
        }
        System.out.println(""); 
        for(Integer i = 0; i < ara.size(); i++)
        {
            for(Integer j = 0; j < fc; j++)
            {
                if( j > 0 ) System.out.print(" "); 
                System.out.print( ara.get(i).get(j) ); 
            }
            System.out.println(""); 
        }
        System.out.println(""); 
    }

    void single_solve()
    {
        get_question();
        ara = new ArrayList < ArrayList <Integer> > ();
        for(Integer r1 = 0; r1 < dt[id1]; r1++)
        {
            for(Integer r2 = 0; r2 < dt[id2]; r2++)
            {
                if( table[id1][r1][c1].intValue() == table[id2][r2][c2].intValue() ) add_row(r1, r2);
            }
        }
        Collections.sort( ara, new cmp() );
        show();
    }

    public void solve(int cs)
    {
        System.out.println("Test: " + cs);
        get_int();
        n = vint.get(0);
        for(Integer t = 0; t < n; t++)
        {
            get_str();
            name[t] = vstr.get(0);
            get_int();
            cl[t] = vint.get(0);
            dt[t] = vint.get(1);
            get_str();
            for(Integer j = 0; j < cl[t]; j++) col[t][j] = vstr.get(j);
            for(Integer i = 0; i < dt[t]; i++)
            {
                get_int();
                for(Integer j = 0; j < cl[t]; j++) table[t][i][j] = vint.get(j); 
            }
        }
        get_int();
        q = vint.get(0);
        for(Integer z = 0; z < q; z++) single_solve();
    }
    @Override
    public void run() 
    {
        ///test case
        get_int();
        T = vint.get(0);
        for(Integer t = 1; t <= T; t++) solve(t);
    }
}

class cmp implements Comparator < ArrayList <Integer> > 
{ 
    public int compare(ArrayList <Integer> a, ArrayList <Integer> b) 
    { 
        for(Integer i = 0; i < a.size(); i++)
        {
            if( a.get(i) < b.get(i) ) return -1;
            if( a.get(i) > b.get(i) ) return +1;
        }
        return 0;
    } 
}
