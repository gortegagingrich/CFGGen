int  I=1, J=2, K=4, M=6, N=8, L=0,F, G;
        int S= I+J;
        K= S+1;
        if(( K+J) != (M + F)) {
        F= M+ N;
        G= M*N;
        while( I <= N && G<( F+I)) {
        K= K+N;
        F= K* N;
        I= I +1;
        }
        }
        else
        F= M-N;
        do{

        K= G*M;
        if( N<F)
        G= H + N;
        F= G-M;
        N=N+1;
        }
        while( N<=20);
        return  G;