int  I=1, J=2, K=4, M=6, N=8, L=0,F, G;
        int S= I+J;
        K= S+1;
        while( I <= N && J<10){
        K= K+N;
        F= K* N;
        I= I +1;
        if (J< M)
        J=J+2;
        else
        J=J+1;
        }

        if(( K+J) != (M + F)) {
        F= M+ N;
        G= M*N;
        do{
        K= G*M;
        F= G-M;
        N=N+1;
        }
        while( N<=20);
        }
        else
        { F= M-N;
        if(F<M || G<20)
        G= M+N;
        else
        G= M-N;
        }
        do{

        K= G*M;
        F= G-M;
        N=N+1;
        }
        while( N<=20);

        if( N<F)
        G= H + N;
        return  G;