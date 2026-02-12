package com.example.asd2.Service;

import com.example.asd2.Repository.SpendingRepository;
import com.example.asd2.dto.CategorySummary;
import com.example.asd2.dto.SpendingRequest;
import com.example.asd2.dto.SpendingResponse;
import com.example.asd2.model.Spending;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@AllArgsConstructor
@Service
public class SpendingService {
    @Autowired
    private SpendingRepository spendingRepository;

    public SpendingResponse addSpending(SpendingRequest request){
        Spending spending = new Spending();
        spending.setCategory(request.getCategory());
        spending.setAmount(request.getAmount());
        spending.setPaymentMethod(request.getPaymentMethod());
        spending.setComment(request.getComment());
        spending.setCreatedAt(LocalDateTime.now());
        spendingRepository.save(spending);
        return getStatistics();
    }

    public List<Spending> getTransactions(){
        return spendingRepository.findAll(Sort.by(Sort.Direction.DESC,"createdAt"));
    }


    public SpendingResponse getStatistics(){
        SpendingResponse spendingResponse = new SpendingResponse();

        //osszes koltes
        List<Spending> allSpendings = spendingRepository.findAll();
        Long total = allSpendings.stream()
                .mapToLong(Spending::getAmount)
                .sum();

        spendingResponse.setTotalSpending(total);

        //napi 00:00-tol
        LocalDateTime startOfDay = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        List<Spending> dailySpending = spendingRepository.findByCreatedAtAfter(startOfDay);
        Long dailyTotal = dailySpending.stream()
                .mapToLong(Spending::getAmount)
                .sum();

        spendingResponse.setDailySpending(dailyTotal);

        //heti hetfotol
        LocalDateTime startOfTheWeek = LocalDateTime.now()
                .minusDays(LocalDateTime.now().getDayOfWeek().getValue()-1)
                .truncatedTo(ChronoUnit.DAYS);
        List<Spending> weeklySpend = spendingRepository.findByCreatedAtAfter(startOfTheWeek);
        Long weeklyTotal = weeklySpend.stream()
                .mapToLong(Spending::getAmount)
                .sum();
        spendingResponse.setWeeklySpending(weeklyTotal);

        LocalDateTime startOfMotnh = LocalDateTime.now()
                .withDayOfMonth(1)
                .truncatedTo(ChronoUnit.DAYS);

        List<Spending> monthlySpending = spendingRepository.findByCreatedAtAfter(startOfMotnh);
        Long monthlyTotal = monthlySpending.stream()
                .mapToLong(Spending::getAmount)
                .sum();

        spendingResponse.setMonthlySpending(monthlyTotal);

        List<Object[]> categorySum = spendingRepository.getCategorySummary();
        List<CategorySummary> summaries = categorySum.stream()
                .map(arr -> new CategorySummary((String) arr[0], (long) arr[1]))
                .toList();

        if(total > 0){
            summaries.forEach(summary ->{
                double percentage = (summary.getTotal()*100.0)/total;
                summary.setPercentage((double)(Math.round(percentage*100.0)/100));
            });
        }
        spendingResponse.setSummary(summaries);

        return spendingResponse;

    }
}
