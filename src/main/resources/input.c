int a = 3;
int *b = &a;
int *f = &a;

int c = *b + 12;

if (*f < *b) {
    a = c + *b;
} else  {
    c = *b;
}