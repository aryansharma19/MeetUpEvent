import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class MeetUpEvent {
    public static class Event{
        //The initial value will be 1 because the organiser,will always be a part of the event;
        private AtomicLong guestCount = new AtomicLong(1);
        private String eventName;

        public void setEventName(String eventName){
            this.eventName = eventName;
        }

        public String getEventName(){
            return eventName;
        }

        public synchronized void addingGuest(int numberOfGuests){
            if(numberOfGuests == 1){
                 guestCount.incrementAndGet();
            }
            else{
                guestCount.addAndGet(numberOfGuests);
            }
        }

        public synchronized void removingGuests(int numberOfGuests){
            if(numberOfGuests == 1){
                guestCount.decrementAndGet();
            }
            else{
                boolean stop = false;
                while(!stop){
                    long currentGuestCount = guestCount.get();
                    long newGuestCount = currentGuestCount - numberOfGuests;
                    stop = guestCount.compareAndSet(currentGuestCount,newGuestCount);
                }
            }
        }

        public AtomicLong getCount(){
            return guestCount;
        }
    }

    public static void main(String[] args){
       Event event = new Event();
       event.setEventName("Hackathon");

       Thread t1 = new Thread(new Runnable() {
           @Override
           public void run() {
               event.addingGuest(4);
               //System.out.println(Thread.currentThread().getName()+" : "+event.getCount());
           }
       });

       Thread t2 = new Thread(new Runnable() {
           @Override
           public void run() {
               event.addingGuest(4);
               //System.out.println(Thread.currentThread().getName()+" : "+event.getCount());
               event.removingGuests(2);
               //System.out.println(Thread.currentThread().getName()+" : "+event.getCount());
           }
       });

       Thread t3 = new Thread(new Runnable() {
           @Override
           public void run() {
               event.addingGuest(4);
               //System.out.println(Thread.currentThread().getName()+" : "+event.getCount());
               event.addingGuest(4);
               //System.out.println(Thread.currentThread().getName()+" : "+event.getCount());
           }
       });

       Thread t4 = new Thread(new Runnable() {
           @Override
           public void run() {
               event.addingGuest(4);
               //System.out.println(Thread.currentThread().getName()+" : "+event.getCount());
               event.removingGuests(4);
               //System.out.println(Thread.currentThread().getName()+" : "+event.getCount());
           }
       });

       t1.setName("User1");
       t2.setName("user2");
       t3.setName("User3");
       t4.setName("User4");


       t1.start();
       //sleep(2);
       t2.start();
       //sleep(3);
       t3.start();
       //sleep(4);
       t4.start();
       //sleep(5);

       System.out.println("Welcome to "+event.getEventName());
       System.out.println("Total "+event.getCount()+" people will be participating in the event");
    }

    public static void sleep(int seconds){
        try {
            TimeUnit.MILLISECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
