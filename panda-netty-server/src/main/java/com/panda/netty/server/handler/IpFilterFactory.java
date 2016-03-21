package com.panda.netty.server.handler;

import java.net.InetSocketAddress;

import io.netty.handler.ipfilter.IpFilterRule;
import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.RuleBasedIpFilter;

public class IpFilterFactory {
	public static RuleBasedIpFilter createRuleBasedIpFilter() {
		return new RuleBasedIpFilter(new IpFilterRule() {
			@Override
			public IpFilterRuleType ruleType() {
				return IpFilterRuleType.ACCEPT;
			}

			@Override
			public boolean matches(InetSocketAddress paramInetSocketAddress) {
				return true;
			}
		}, new IpFilterRule() {
			@Override
			public IpFilterRuleType ruleType() {
				return IpFilterRuleType.REJECT;
			}

			@Override
			public boolean matches(InetSocketAddress paramInetSocketAddress) {
				return false;
			}
		});
	}
}
