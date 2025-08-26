

package com.example.insurance.mis.mvp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.insurance.mis.mvp.entities.Policy;

public interface PolicyRepository extends JpaRepository<Policy, Long>{
      Optional<Policy> findByPolicyCustomerIdAndPolicyCode(String policyCustomerId, String policyCode);
      
      Optional<Policy> findByPolicyCustomerId(String policyCustomerId);

      Optional<List<Policy>> findAllByPolicyCustomerId(String policyCustomerId);

      Optional<List<Policy>> findAllByPolicySellerId(String policySellerId);
      
      Optional<List<Policy>> findByPolicySellerIdIsNotNull();

      Optional<Policy> findByPolicyCode(String policyCode);

      Optional<List<Policy>> findByClaimStatus(String claimStatus);
}
