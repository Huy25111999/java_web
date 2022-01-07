package actors;

import java.util.Set;

import static java.util.Objects.requireNonNull;

public final class Messages {

    public static final class CpuSocket {

        final String id;
        final String userId;
        public CpuSocket(String id, String userId){
            this.id = requireNonNull(id);
            this.userId = requireNonNull(userId);
        }

        @Override
        public String toString() {
            return "CpuSocket(" + userId.toString() + ")";
        }
    }

}
