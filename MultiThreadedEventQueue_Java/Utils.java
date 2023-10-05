
public class Utils {
 
  static int primeLowerThan(int num) {
    boolean isPrime = false;
    while(!isPrime && num > 0) {
      isPrime = true;
      for(int i = 2; i*i <= num; i++) {
        if (num % i == 0) {
          isPrime = false;
          break;
        }
      }
      if (!isPrime)
        num--;
    }
    return num;
  }
  static int factLowerThan(int n) {
    int fact = 1, i;
    for(i = 1; i < n; i ++) {
      if (fact * i > n) {
        break;
      }
      fact *= i;
    }
    return i-1;
  }
  static int squareLowerThan(int n) {
    int i;
    for(i = 0; i*i <= n; i ++);
    return i-1;
  }
  static int fiboLowerThan(int n) {
    int index = 2;
    int fibo1 = 0;
    int fibo2 = 1;
    int fibosum = 1;
    while (fibosum < n) {
      fibo1 = fibo2;
      fibo2 = fibosum;
      fibosum = fibo1+fibo2;
      index++;
    }
    return index-1;
  }
}
