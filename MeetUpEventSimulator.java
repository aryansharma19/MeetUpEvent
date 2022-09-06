import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class MeetUpEventSimulator {

    public static class MeetUpEvent{
        private String eventName;
        private AtomicLong guestCount = new AtomicLong(1);//1 for organiser

        public MeetUpEvent(String eventName){
            this.eventName = eventName;
        }

        public void attending(int count){
            if(count == 1){
                guestCount.incrementAndGet();
            }
            else{
                guestCount.addAndGet(count);
            }
        }

        public void nonAttending(int count){
            if(count == 1){
                guestCount.decrementAndGet();
            }
            else{
                boolean updated = false;
                while(!updated) {
                    long currentGuestCount = guestCount.get();
                    long newGuestCount = currentGuestCount - count;
                    updated = guestCount.compareAndSet(currentGuestCount,newGuestCount);
                }

            }
        }

        public long getCount(){
            return guestCount.get();
        }
    }

    public static void main(String[] args) {
        MeetUpEvent jugBoston = new MeetUpEvent("The Boston Java User group");
        Thread user1 = new Thread(new Runnable() {
            @Override
            public void run() {
                jugBoston.attending(4);
                System.out.println(Thread.currentThread().getName()+":"+jugBoston.getCount());
            }
        });

        Thread user2 = new Thread(new Runnable() {
            @Override
            public void run() {
                jugBoston.attending(3);
                System.out.println(Thread.currentThread().getName()+":"+jugBoston.getCount());
                jugBoston.nonAttending(3);
                System.out.println(Thread.currentThread().getName()+":"+jugBoston.getCount());
            }
        });

        Thread user3 = new Thread(new Runnable() {
            @Override
            public void run() {
                jugBoston.attending(1);
                System.out.println(Thread.currentThread().getName()+":"+jugBoston.getCount());
            }
        });


        user1.setName("user1");
        user2.setName("user2");
        user3.setName("user3");

        user1.start();
        sleep(1);
        user2.start();
        sleep(2);
        user3.start();
        sleep(2);

        System.out.println("The total user count is :"+jugBoston.getCount());
    }

    public static void sleep(int i){
        try {
            TimeUnit.SECONDS.sleep(i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
