import java.util.*;
import java.io.*;

class sttuple
{
	String id;
	String symbol;
	int addr;
	int length;
	char rel;

	sttuple(String i,String s,int a,int l,char c)
	{
		id = i;
		symbol = s;
		addr = a;
		length = l;
		rel = c;
	}

}

class lttuple
{
	String id;
	String literal;
	int address;
	int length;
	int flag;
	lttuple(String i,String l,int a,int len,int f)
	{
		id = i;
		literal = l;
		address = a;
		length = len;
		flag = f;
	}
}



class a_pass1
{
	int lc,temp1,temp2,basereg;
	Scanner sc1,sc2;
	PrintWriter output;
	List<sttuple> st;
	List<lttuple> lt;

	void initialize()throws Exception
	{
		st = new LinkedList<>();
		lt = new LinkedList<>();
		lc = 0;
		temp1 = 0;
		temp2 = 0;
	}

	void reader()throws Exception
	{
		sc1 = new Scanner(new File("F:/SPR/SPR/Assembler pass1/sourcecode.txt"));
		output = new PrintWriter(new File("intermediatecode.txt"));
		while(sc1.hasNextLine())
		{
			String str1 = sc1.nextLine();
			StringTokenizer strtok1 = new StringTokenizer(str1," ",false);
			String arr1[] = new String[strtok1.countTokens()];
			for(int i=0;i<arr1.length;i++)
			{
				arr1[i] = strtok1.nextToken();
				
			}

			
			if((searchpot(arr1)) == false)
			{
				searchmot(arr1);
			}
		}
		output.close();
	}

	boolean searchpot(String array[])throws Exception
	{
		switch(array[1])
		{
			case "START":
			{
				String v = seq1();
				lc = Integer.parseInt(array[2]);
				st.add(new sttuple(v,array[0],Integer.parseInt(array[2]),1,'R'));
				output.write(lc+"\t"+array[0]+"\t"+array[1]+"\t"+array[2]+"\n");
				return true;
			}
			case "LTORG":
			{
				int flag = 0;
				for(lttuple i: lt)
				{
					while(flag == 0)
					{
						if(i.flag == -1)
						{
							if(lc%8 == 0)
							{
								output.write(lc+"\t\t\t"+i.literal+"\n");
								i.flag = 0;
								flag = 1;	
							}
							else
							{
								lc = lc +2;
							}	
						}
					}
				}
				return true;
			}
			case "USING":
			{
					output.write(lc+"\t\t\t"+array[1]+"\t"+array[2]+"\n");
					String str2 = array[2];
					String str3 = str2.substring(2);
					basereg = Integer.parseInt(str3);
					return true;
			}
			case "DC":
			{
				int inc;
				String s1 = array[0];
				String s2 = array[2];
				StringTokenizer st3 = new StringTokenizer(s2,"'",false);
				String strarr[] = new String[st3.countTokens()];
				for(int i=0;i<strarr.length;i++)
				{
					strarr[i] = st3.nextToken();
				}
				if(strarr[0].equals("F"))
				{
					inc = 4;
				}
				else
				{
					inc = 2;
				}

				for(sttuple i: st)
				{
					if(i.symbol.equals(s1))
					{
						i.addr = lc;
						i.length = inc;	
					}
				}
				lc = lc +inc;
				output.write(lc+"\t\t\t"+strarr[1]+"\n");
				return true;
			}
			case "DS":
			{
				int l=0;
				String s = array[2];
				String sbr = array[0];
				String ss = s.substring(0,1);
				String sss = s.substring(1,2);
				int x = Integer.parseInt(ss);
				switch(sss)
				{
					case "H":
					{
						l = 2;
						break;
					}
					case "F":
					{
						l = 4 * x;
					}
				}
				for(sttuple i: st)
				{
					if(i.symbol.equals(sbr))
					{
						i.addr = lc;	
						i.length = l;
					}
				}
				return true;
			}
			case "END":
			{
				output.write(lc+"\t\t\t"+array[1]+"\n");
				for(lttuple i: lt)
				{
					if(i.flag == -1)
					{
						output.write(lc+"\t\t\t"+i.literal+"\n");
						lc = lc+4;
					}
				}
				return true;
			}
			default:
			{
				return false;
			}
		}
	}

	int searchmot(String array[])throws Exception
	{
		String str5 = array[1];
		sc2 = new Scanner(new File("F:/SPR/SPR/Assembler pass1/mot.txt"));
		String moptype="";
		while(sc2.hasNextLine())
		{
			String str4 = sc2.nextLine();
			StringTokenizer strtok2 = new StringTokenizer(str4," ",false);
			String arr2[] = new String[strtok2.countTokens()];
			for(int i=0;i<arr2.length;i++)
			{
				arr2[i] = strtok2.nextToken();
			}
			String str6 = arr2[0];
			if(str5.equals(str6))
			{
				moptype = arr2[3];
				break;
			}
		}

		if(moptype.equals("RX"))
		{
			lc = lc + 4;
			String str7 = array[2];
			StringTokenizer strtok3 = new StringTokenizer(str7,",",false);
			String arr3[] = new String[strtok3.countTokens()];
			for(int i=0;i<arr3.length;i++)
			{
				arr3[i] = strtok3.nextToken();
			}
			String str8 = arr3[1];
			if(str8.startsWith("="))
			{
				int len=0;
				if(str8.contains("F"))
				{
					len = 4;
				}
				else if(str8.contains("H"))
				{
					len = 2;
				}
				String v = seq2();
				String s = str8.substring(3,4);
				lt.add(new lttuple(v,s,lc,len,-1));
				output.write(lc+"\t\t\t"+array[1]+"\t\t"+arr3[0]+","+v+"(0,"+basereg+")\n");
			}
			else
			{
				String v = seq1();
				st.add(new sttuple(v,str8,lc,0,'R'));
				output.write(lc+"\t\t\t"+array[1]+"\t\t"+arr3[0]+","+v+"(0,"+basereg+")\n");
			}
		}
		else if(moptype.equals("RR"))
		{
			lc = lc + 2;
			output.write(lc+"\t\t\t"+array[1]+"\t\t"+array[2]+"\n");
		}
	return 0;
	}


	String seq1()
	{
		String var1 = "#";
		String var2 = Integer.toString(temp1++);
		String var3 = var1.concat(var2);
		return var3;
	}

	String seq2()
	{
		String var1 = "$";
		String var2 = Integer.toString(temp2++);
		String var3 = var1.concat(var2);
		return var3;
	}

	void printst()throws Exception
	{
		output = new PrintWriter(new File("F:/SPR/SPR/Assembler pass1/st.txt"));
		System.out.println("Symbol Table:");
		System.out.println("ID\t\tSymbol\t\tAddress\t\tLength\t\tRelocatable");
		for(sttuple i: st)
		{
			System.out.println(i.id+"\t\t"+i.symbol+"\t\t"+i.addr+"\t\t"+i.length+"\t\t"+i.rel);
			output.write(i.id+"\t\t"+i.symbol+"\t\t"+i.addr+"\t\t"+i.length+"\t\t"+i.rel+"\n");
		}
		output.close();
	}
	void printlt()throws Exception
	{
		output = new PrintWriter(new File("F:/SPR/SPR/Assembler pass1/lt.txt"));
		System.out.println("Literal Table:");
		System.out.println("ID\t\tLiteral\t\tAddress\t\tLength\t\tFlag");
		for(lttuple i: lt)
		{
			System.out.println(i.id+"\t\t"+i.literal+"\t\t"+i.address+"\t\t"+i.length+"\t\t"+i.flag);
			output.write(i.id+"\t\t"+i.literal+"\t\t"+i.address+"\t\t"+i.length+"\n");
		}
		output.close();
	}

	public static void main(String args[])throws Exception
	{
		a_pass1 obj = new a_pass1();

		obj.initialize();
		obj.reader();
		obj.printst();
		obj.printlt();
	}
}