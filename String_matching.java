import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;




public class String_matching {
	static int num_chars =25600;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Read inputs
		String pattern,text = "";
		Scanner scan1 =null;
		ArrayList<Integer> valid_shifts = new ArrayList<Integer>();
		long startTime=0;
		long endTime = 0;
		long naive_time=0,rabin_karp_time=0,kmp_time=0,boyer_moore_time=0;
		if(args.length<2)
		{
			System.out.println("Invalid Input Format");
			System.exit(0);
		}
		pattern = args[0];
		try {
			scan1 = new Scanner(new FileInputStream(args[1]));
			while (scan1.hasNext()) {
				text = text +  scan1.next();
				
				
			}
			int prime = 113;
			startTime = System.currentTimeMillis();
			naive_pattern_search(pattern,text,valid_shifts);
			endTime = System.currentTimeMillis();
			naive_time = (endTime - startTime);

			startTime = System.currentTimeMillis();
			rabin_karp_pattern_serach(pattern,text,prime,valid_shifts);
			endTime = System.currentTimeMillis();
			rabin_karp_time = (endTime - startTime);

			startTime = System.currentTimeMillis();
			kmp_pattern_search(pattern,text,valid_shifts);
			endTime = System.currentTimeMillis();
			kmp_time = (endTime - startTime);

			startTime = System.currentTimeMillis();
			boyer_moore_pattern_search(pattern,text,valid_shifts);
			endTime = System.currentTimeMillis();
			boyer_moore_time = (endTime - startTime);

			System.out.print(valid_shifts.size());
			System.out.print(" ");
			System.out.print(naive_time);
			System.out.print(" ");
			System.out.print(rabin_karp_time);
			System.out.print(" ");
			System.out.print(kmp_time);
			System.out.print(" ");
			System.out.println(boyer_moore_time);
			if(valid_shifts.size()<=20)
			{
				for(int i=0;i<valid_shifts.size();i++)
				{

					System.out.print((valid_shifts.get(i)));
					System.out.print(" ");
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	/*NAIVE PATTERN SEARCH ALGORITHM*/

	//Input: pattern,text in which pattern is matched, array list to store valid shift points.
	static void naive_pattern_search(String pattern,String text, ArrayList<Integer> valid_shifts)
	{

		int n,m;
		n= text.length();
		m = pattern.length();
		for(int s=0;s<=n-m;s++)
		{
			if(pattern.equals(text.substring(s, m+s)))
			{
				valid_shifts.add(s);
			}
		}

		/*for(int i=0;i<valid_shifts.size();i++)
		{
			System.out.println((valid_shifts.get(i)));
		}*/
	}


	/* RABIN KARP ALGORITHM*/

	//Input: pattern,text in which pattern is matched,prime number for calculating hash value, array list to store valid shift points.
	static void rabin_karp_pattern_serach(String pattern,String text,int prime, ArrayList<Integer> valid_shifts)
	{
		int n,m;
		n= text.length();
		m = pattern.length();
		int pattern_hash = 0,text_hash=0,hash=1;
		int num_of_chars = 256;
		for(int i=0;i<m-1;i++)
		{
			hash = (hash * num_of_chars)%prime;
		}
		for(int i=0;i<m;i++)
		{
			pattern_hash = (num_of_chars*pattern_hash + pattern.charAt(i))%prime;
			text_hash = (num_of_chars*text_hash + pattern.charAt(i))%prime;

		}
		int r=0;
		for(int s=0;s<=n-m;s++)
		{
			if(pattern_hash == text_hash)
			{
				if(pattern.equals(text.substring(s, m+s)))
				{
					valid_shifts.set(r, s);
					r++;
				}
			}
			if(s < n-m)
			{
				text_hash = (num_of_chars*(text_hash - text.charAt(s)*hash) + text.charAt(s+m))%prime;
				if(text_hash < 0)
				{
					text_hash = text_hash + prime;
				}
			}
		}
		/*for(int i=0;i<valid_shifts.size();i++)
		{
			System.out.println((valid_shifts.get(i)));
		}*/
	}


	/*KMP PATTERN SEARCH */

	//Input: pattern,text in which pattern is matched, array list to store valid shift points.
	private static void kmp_pattern_search(String pattern, String text, ArrayList<Integer> valid_shifts) {

		int n,m;
		n= text.length();
		m = pattern.length();
		int[] pi = new int[m];
		compute_prefix_function(pattern,m,pi);
		int q = 0,r=0;
		for(int i=0;i<n;i++)
		{
			while(q>0 && pattern.charAt(q)!=text.charAt(i))
			{
				q = pi[q];
			}
			if(pattern.charAt(q)==text.charAt(i))
			{
				q++;
			}
			if(q==m)
			{
				valid_shifts.set(r, i-m+1);
				r++;
				q = pi[q-1];
				i--;
			}
		}
		/*for(int i=0;i<valid_shifts.size();i++)
		{
			System.out.println((valid_shifts.get(i)));
		}*/
	}

	//Function to compute prefix function of the pattern
	//Input: pattern, size of pattern, and the array to store the prefix function values
	private static void compute_prefix_function(String pattern,int m,
			int[] pi) {

		pi[1]= 0;
		int k = 0;
		for(int q=2;q<m;q++)
		{
			while(k>0 && pattern.charAt(k+1)!=pattern.charAt(q))
			{
				k = pi[k];
			}
			if(pattern.charAt(k+1) == pattern.charAt(q))
			{
				k++;
			}
			pi[q] = k;
		}
	}

	/* BOYER MOORE PATTERN SEARCH */

	//Maximum function return the maximum amongst two int numbers
	static int maximum (int a, int b) { return (a > b)? a: b; }

	//Bad character heuristics for boyer moore algorithm
	//Input string pattern,size of the pattern,and array to store bad characters
	static void bad_characters(String pattern,int size,int[] bad_characters)
	{

		for (int i = 0; i <num_chars ; i++)
		{
			bad_characters[i] = -1;
		}
		for (int i = 0; i < size; i++)
		{
			bad_characters[(int) pattern.charAt(i)] = i;
		}
	}

	//Boyer moore pattern search algorithm
	//Input: pattern,text in which pattern is matched, array list to store valid shift points.
	private static void boyer_moore_pattern_search(String pattern, String text, ArrayList<Integer> valid_shifts) {
		// TODO Auto-generated method stub
		int n,m;
		n= text.length();
		m = pattern.length();
		int[] bad_characters = new int[num_chars];
		bad_characters(pattern,m,bad_characters);
		int s =0,r=0;
		while(s<=n-m)
		{
			int j = m-1;
			while(j>=0 && pattern.charAt(j)==text.charAt(s+j))
			{
				j--;
			}
			if(j<0)
			{
				valid_shifts.set(r, s);
				r++;
				s += (s+m < n)? m-bad_characters[text.charAt(s+m)] : 1;
			}
			else
			{
				s += maximum(1, j - bad_characters[text.charAt(s+j)]);
			}
		}
		/*for(int i=0;i<valid_shifts.size();i++)
		{
			System.out.println((valid_shifts.get(i)));
		}*/
	}
}
