package cristina.tech.fancydress.store.service;

import cristina.tech.fancydress.store.repository.DressRepository;
import cristina.tech.fancydress.store.view.DressDetailView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
public class TrendingDressesService {

    @Autowired
    private DressRepository dressRepository;

    @Transactional(readOnly = true)
    public List<DressDetailView> getTrending(int count, String interval) {
        return dressRepository.findTopNTrendingTimeInterval(count, interval)
                .stream().map(DressDetailView::map).collect(toList());
    }
}
