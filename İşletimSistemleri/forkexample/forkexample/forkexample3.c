#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>
int main()
{
 
    // make two process which run same
    // program after this instruction
    printf("Parent Process ID : %d\n", getpid());
    pid_t p = fork();
    int c;
    if(p<0){
      perror("fork fail");
      exit(1);
    }else if(p==0){ // inside child process
       printf("Returned from fork = %d :: Hello world from child process!, process_id(pid) = %d \n",p,getpid());
       sleep(4);
    }else{ // inside parent process
       wait(&c);
       printf("Returned from fork = %d :: Inside parent process, process_id(pid) = %d \n",p,getpid());
    }
    return 0;
}