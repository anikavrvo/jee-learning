package medaid.hospitalauths.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import medaid.hospitalauths.TestSupport;
import medaid.hospitalauths.repository.PlanRepository;
import org.junit.Test;

public class MemberEligibilityServiceTest {
    @Test
    public void delegatesHospitalBenefitCheck() throws Exception {
        PlanRepository repository = mock(PlanRepository.class);
        when(repository.hasHospitalBenefit("M001")).thenReturn(true);
        MemberEligibilityService service = new MemberEligibilityService();
        TestSupport.inject(service, "planRepository", repository);

        assertTrue(service.hasHospitalBenefit("M001"));
        verify(repository).hasHospitalBenefit("M001");
    }

    @Test
    public void returnsIneligibleWhenRepositoryFindsNoBenefit() throws Exception {
        PlanRepository repository = mock(PlanRepository.class);
        when(repository.hasHospitalBenefit("M001")).thenReturn(false);
        MemberEligibilityService service = new MemberEligibilityService();
        TestSupport.inject(service, "planRepository", repository);

        assertFalse(service.hasHospitalBenefit("M001"));
    }
}
