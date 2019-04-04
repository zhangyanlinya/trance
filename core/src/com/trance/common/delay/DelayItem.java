package com.trance.common.delay;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayItem<T extends Runnable> implements Delayed {
    
    /**
     * 任务对象
     */
    private T task;
    
    /**
     * 到期时间
     */
    private long expire;
    
    public DelayItem() {
    }
    
    public DelayItem (T task, long duration){
	this.task = task;
	this.expire = System.currentTimeMillis() + duration;
    }
    
    @Override
    public int compareTo(Delayed o) {
	 return (int) (this.expire - ((DelayItem<?>) o).getExpire());
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long diff = expire - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    public T getTask() {
        return task;
    }

    public void setTask(T task) {
        this.task = task;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((task == null) ? 0 : task.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	DelayItem<?> other = (DelayItem<?>) obj;
	if (task == null) {
	    if (other.task != null)
		return false;
	} else if (!task.equals(other.task))
	    return false;
	return true;
    }
}
