import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AuctionList {
    private int total;
    private List<AuctionItem> lots;
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class AuctionItem {
        private String itemId;
        private int amount;
        private long startPrice;
        private long currentPrice;
        private long buyoutPrice;
        private Date startTime;
        private Date endTime;
        private AdditionalInfo additional;
        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        public static class AdditionalInfo {
            private List<String> bonus_properties;
            private double md_k;
            private double ndmg;
            private int it_transf_count;
            private int qlt;
            private int ptn;
            private double stats_random;
            private Double upgrade_bonus;
            private long spawn_time;
            private String buyer;
        }
    }
}