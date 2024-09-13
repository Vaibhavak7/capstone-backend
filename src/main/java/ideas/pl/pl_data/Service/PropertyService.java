package ideas.pl.pl_data.Service;

import ideas.pl.pl_data.DTO.PropertyDTO;
import ideas.pl.pl_data.Entity.Property;
import ideas.pl.pl_data.Repository.PropertyRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    // Get all properties as DTOs
    public List<PropertyDTO> getAllProperties1() {
        return propertyRepository.findAllPropertiesAsDTO();
    }
    public List<PropertyDTO> getAllProperties() {
        return propertyRepository.findBy();
    }

    // Get property by ID as DTO
    public Optional<PropertyDTO> getPropertyById(int propertyId) {
        return propertyRepository.findByPropertyId(propertyId);
    }

    public Optional<PropertyDTO> getPropertyByIdWithOwner(int propertyId) {
        return propertyRepository.findByPropertyIdWithOwner(propertyId);
    }

    // Create a new property
    public Property createProperty(Property property) {
        return propertyRepository.save(property);
    }

    public Optional<Property> updateProperty(int propertyId, Property updatedProperty) {
        return propertyRepository.findById(propertyId).map(property -> {
            property.setPropertyName(updatedProperty.getPropertyName());
            property.setAddress1(updatedProperty.getAddress1());
            property.setAddress2(updatedProperty.getAddress2());
            property.setCity(updatedProperty.getCity());
            property.setState(updatedProperty.getState());
            property.setZipcode(updatedProperty.getZipcode());
            property.setDescription(updatedProperty.getDescription());
            property.setRent(updatedProperty.getRent());
            property.setSecurityDeposit(updatedProperty.getSecurityDeposit());
            property.setHowOldProperty(updatedProperty.getHowOldProperty());
            return propertyRepository.save(property);
        });
    }

    // Update Property
//    public Optional<Property> updateProperty1(int propertyId, Property updatedProperty) {
//        return propertyRepository.findById(propertyId).map(existingProperty -> {
//            // Copy properties from updatedProperty to existingProperty, ignoring propertyId
//            BeanUtils.copyProperties(updatedProperty, existingProperty, "propertyId");
//            return propertyRepository.save(existingProperty);
//        });
//    }

    // Delete property by ID
    public void deleteProperty(int propertyId) {
        propertyRepository.deleteById(propertyId);
    }

    // Additional search methods
    public List<PropertyDTO> findByFeatures(String feature) {
        return propertyRepository.findByFeatures(feature);
    }

    public List<PropertyDTO> findByPropertyNameLike(String name) {
        return propertyRepository.findByPropertyNameLike(name);
    }

    public List<PropertyDTO> findByAddressLike(String address) {
        return propertyRepository.findByAddressLike(address);
    }
}

