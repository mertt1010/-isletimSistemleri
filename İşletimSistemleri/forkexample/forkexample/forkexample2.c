#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>
int main()
{
 
    // make two process which run same
    // program after this instruction
  
    printf("Inside parent process, process_id(pid) = %d \n",getpid());
    pid_t p = fork();
    if(p<0){
      perror("fork fail");
      exit(1);
    }else if(p==0){ // inside child process
       printf("Inside child process, Hello world!, process_id(pid) = %d \n",getpid()); 
    }else{ // inside parent process
       printf("Inside parent process, process_id(pid) = %d \n",getpid());
    }
    return 0;
}