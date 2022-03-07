// Teoria Współbieżnośi, implementacja problemu 5 filozofów w node.js
// Opis problemu: http://en.wikipedia.org/wiki/Dining_philosophers_problem
//   https://pl.wikipedia.org/wiki/Problem_ucztuj%C4%85cych_filozof%C3%B3w
// 1. Dokończ implementację funkcji podnoszenia widelca (Fork.acquire).
// 2. Zaimplementuj "naiwny" algorytm (każdy filozof podnosi najpierw lewy, potem
//    prawy widelec, itd.).
// 3. Zaimplementuj rozwiązanie asymetryczne: filozofowie z nieparzystym numerem
//    najpierw podnoszą widelec lewy, z parzystym -- prawy.
// 4. Zaimplementuj rozwiązanie z kelnerem (według polskiej wersji strony)
// 5. Zaimplementuj rozwiążanie z jednoczesnym podnoszeniem widelców:
//    filozof albo podnosi jednocześnie oba widelce, albo żadnego.
// 6. Uruchom eksperymenty dla różnej liczby filozofów i dla każdego wariantu
//    implementacji zmierz średni czas oczekiwania każdego filozofa na dostęp
//    do widelców. Wyniki przedstaw na wykresach.



var Fork = function() {
    this.state = 0;
    return this;
}

Fork.prototype.acquire = function (cb) {
    let fork = this;
    let startTime = 1;

    let handler = function (waitTime) {
        if (fork.state === 0) {
            fork.state = 1;
            cb();
        }
        else {
            setTimeout(function () {
                 handler(waitTime * 2)
            }, waitTime);
        }
    };
    setTimeout(function () {
        handler( startTime * 2)
    }, startTime);
};

Fork.prototype.release = function() {
    this.state = 0;
}

var Philosopher = function(id, forks) {
    this.id = id;
    this.forks = forks;
    this.f1 = id % forks.length;
    this.f2 = (id+1) % forks.length;
    return this;
}

Philosopher.prototype.startNaive = function (count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id;

    let eatingTime = 100;
    let loop = function (count) {
        var startTime = new Date().getTime();
        if(count > 0) {
            forks[f1].acquire(function () {
                console.log("Philosopher no: " + id + " has a left fork");
                forks[f2].acquire(function () {
                    var stopTime = new Date().getTime();
                    var time = (stopTime - startTime);
                    //console.log(id+";"+time);
                    console.log("Philosopher no: " + id + "has time: " + time + " [ms]");
                    console.log("Philosopher no: " + id + " has a right fork" );
                    console.log("Philosopher no: " + id + " start eating");
                    setTimeout(function () {
                        forks[f1].release();
                        forks[f2].release();
                        console.log("Philosopher no: " + id + " finished eating");
                        loop(count - 1);
                    }, eatingTime)
                })
            })
        }
    };

    setTimeout(function () {
        console.log("Philosopher no: " + id + " is thinking");
        loop(count)
    }, Math.floor(Math.random() * 10)); // Czas wylosowany, aby program nie zakleszczał się już na początku
}


Philosopher.prototype.startAsym = function(count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id;

    if(id % 2 === 0){
        [f1, f2] = [f2, f1];
    }

    let eatingTime = 100;
    let thinkingTime = 100;

    let loop = function (count) {
        var startTime = new Date().getTime();
        if(count > 0) {
            forks[f1].acquire(function () {
                console.log("Philosopher no: " + id + " has a left fork");
                forks[f2].acquire(function () {
                    var stopTime = new Date().getTime();
                    var time = stopTime - startTime;
                    //console.log(id+";"+time);
                    console.log("Philosopher no: " + id + " time: " + time + " [ms]");
                    console.log("Philosopher no: " + id + " has a right fork" );
                    console.log("Philosopher no: " + id + " start eating");
                    setTimeout(function () {
                        forks[f1].release();
                        forks[f2].release();
                        console.log("Philosopher no: " + id + " finished eating");
                        loop(count - 1);
                    }, eatingTime)
                })
            })
        }
    };

    setTimeout(function () {
        console.log("Philosopher no: " + id + " is thinking");
        loop(count)
    }, thinkingTime);


}

var Conductor = function(n){
    this.state = n-1;
    return this;
}

Conductor.prototype.acquire = function (cb){
    let cond = this;
    let startTime = 1;
    let handler = function (waitTime, cb) {
        if (cond.state > 0) {
            cond.state--;
            cb();
        }
        else {
            setTimeout(function () {
                handler( waitTime * 2, cb)
            }, waitTime);
        }
    };

    setTimeout(function () {
        handler( startTime * 2, cb)
    }, startTime);
}

Conductor.prototype.release = function (){
    this.state ++;
}

Philosopher.prototype.startConductor = function(count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id;

    let n = count;
    let eatingTime = 100;
    let thinkingTime = 100;
    let loop = function (count) {
        var startTime = new Date().getTime();
        if (count > 0) {
            conductor.acquire(function () {
                forks[f1].acquire(function () {
                    console.log("Philosopher no: " + id + " has a left fork");
                    forks[f2].acquire(function () {
                         var stopTime = new Date().getTime();
                         var time = stopTime - startTime;
                        //console.log(id+";"+time);
                        console.log("Philosopher no: " + id + " time: " + time + " [ms]");
                         console.log("Philosopher no: " + id + " has a right fork");
                         setTimeout(function () {
                            forks[f1].release();
                            forks[f2].release();
                            conductor.release(id);
                            console.log("Philosopher no: " + id + " finished eating");
                            loop(count - 1);
                        }, eatingTime)
                    })
                })
            })
        }

    }
    setTimeout(function () {
        console.log("Philosopher no: " + id + " is thinking");
        loop(count)
    }, thinkingTime);
}


function acquireTwoForksAtOnce (f1, f2, cb) {
    let startTime = 1;
    let handler = function (waitTime) {
        if (f1.state === 0 && f2.state === 0) {
            f1.state = 1;
            f2.state = 1;
            cb()
        } else {
            setTimeout(function () {
                handler(waitTime * 2)
            }, waitTime)
        }
    }
    setTimeout(function (){
        handler(startTime*2)
    }, startTime);

}

Philosopher.prototype.startTwoSameTime = function(count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id;

    let eatingTime = 100;
    let thinkingTime = 100;

    let loop = function (count) {
        var startTime = new Date().getTime();
        if (count > 0) {
            acquireTwoForksAtOnce(forks[f1], forks[f2], function () {
                  var stopTime = new Date().getTime();
                  var time = (stopTime - startTime);
                  //console.log(id+";"+time);
                  console.log("Philosopher no: " + id + " time: " + time + " [ms]");
                  console.log("Philosopher no: " + id + " has a left fork" );
                  console.log("Philosopher no: " + id + " has a right fork" );

                setTimeout(function () {
                    forks[f1].release();
                    forks[f2].release();
                    console.log("Philosopher no: " + id + " finished eating");
                    loop(count - 1);
                }, eatingTime)

            })
        }
    }
    setTimeout(function () {
        console.log("Philosopher no: " + id + " is thinking");
        loop(count)
    },thinkingTime);



}

var N = 10;
var forks = [];
var philosophers = []
let i;
let iter_num = 10;
for (i = 0; i < N; i++) {
    forks.push(new Fork());
}

for (i = 0; i < N; i++) {
    philosophers.push(new Philosopher(i, forks));
}
let conductor = new Conductor(N)
/*
for (i = 0; i < N; i++) {
    philosophers[i].startNaive(iter_num);
}
 */

console.log("Asynchronous philosopher: ")
for (i = 0; i < N; i++) {
    philosophers[i].startAsym(iter_num);
}

console.log("Philosopher with conductor: ")
for (i = 0; i < N; i++) {
   //philosophers[i].startConductor(iter_num);
}

console.log("Philosopher with two forks at the same time: ")
for (i = 0; i < N; i++) {
    //philosophers[i].startTwoSameTime(iter_num);
}

