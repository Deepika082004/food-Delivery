package com.example.Mapper;

import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.stream.Collectors;
public class MapperUtils {

        private static final ModelMapper modelMapper = new ModelMapper();

        private MapperUtils() {
        }

        public static <S, T> T map(S source, Class<T> targetClass) {
            if (source == null) return null;
            return modelMapper.map(source, targetClass);
        }
        public static <S, T> List<T> mapList(List<S> sourceList, Class<T> targetClass) {
            if (sourceList == null) return null;
            return sourceList.stream()
                    .map(element -> modelMapper.map(element, targetClass))
                    .collect(Collectors.toList());
        }
        public static <S, T> void mapToExisting(S source, T target) {
            if (source != null && target != null) {
                modelMapper.map(source, target);
            }
        }
    }


